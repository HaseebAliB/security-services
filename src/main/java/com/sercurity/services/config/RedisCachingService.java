package com.sercurity.services.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCachingService {

    private final RedisTemplate redisTemplate;
    private final ObjectMapper mapper;

    public <T> T get(String key, Class<T> entityClass) {
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            return null; // or throw an exception if you prefer
        }

        try {
            String jsonString = (String) o;
            return mapper.readValue(jsonString, entityClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON from Redis", e);
        }
    }

    public void set(String key, Object value) {

        final String jsonString;
        try {
            jsonString = mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) { // required for the case where attempt to insert corrupt data in db, as redis does not follow jpa/hibernate transactional context
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    redisTemplate.opsForValue().set(key, jsonString, Duration.ofMinutes(20));
                }
            });
        } else {
            redisTemplate.opsForValue().set(key, jsonString, Duration.ofMinutes(20));
        }
    }

    public void delete(String key) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    redisTemplate.delete(key);
                }
            });
        } else {
            redisTemplate.delete(key);
        }

    }
}