package com.sercurity.services.services;

import com.sercurity.services.config.RedisCachingService;
import com.sercurity.services.dtos.UserDto;
import com.sercurity.services.dtos.UserResponseDto;
import com.sercurity.services.entitymappers.UserMapper;
import com.sercurity.services.models.User;
import com.sercurity.services.repositories.RoleRepository;
import com.sercurity.services.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private RedisCachingService cachingService;



    @Override
    public Optional<User> findByUserName(String username){
        UserDto userDto = cachingService.get(username, UserDto.class);
        if (userDto != null) {
            User user = UserMapper.toEntity(userDto);
            return Optional.of(user);
        }
        return userRepository.findByUserName(username);

    }
    @Override
    @Transactional
    public User registerUserFromOauth(User user){
        if (user.getPassword() != null)
            user.setPassword(encoder.encode(user.getPassword()));

        cachingService.set(user.getUserName(), UserMapper.toDto(user));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponseDto createUser(UserDto userDto) {
        findByUserName(userDto.getUsername()).ifPresent( user -> {
            throw new RuntimeException("User already exists with username: " + userDto.getUsername());
        });

       User user = User.builder().userName(userDto.getUsername()).email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .role(roleRepository.findByRoleName(userDto.getRole()).get())
                .accountNonLocked(false)
                .accountNonExpired(true)
                .enabled(true)
                .credentialsExpiryDate(LocalDate.now().plusYears(1))
                .accountExpiryDate(LocalDate.now().plusYears(1))
                .isTwoFactorEnabled(false)
                .signUpMethod("email")
                .build();

            User newUser = userRepository.save(user);
            cachingService.set(newUser.getUserName(), UserMapper.toDto(newUser));

        return UserDto.buildResponse(userDto);

    }

    @Override
    @Transactional
    public UserResponseDto updateUser(UserDto userDto) {
        User user =   findByUserName(userDto.getUsername())
                .orElseThrow( () -> new RuntimeException("User not found with username: " + userDto.getUsername()));


        User.UserBuilder builder = User.builder();

        builder.userId(user.getUserId());

        setIfNotNull(builder::userName, userDto.getUsername(), user.getUserName());
        setIfNotNull(builder::email, userDto.getEmail(),user.getEmail());
        setIfNotNull(pw -> builder.password(encoder.encode(pw)), userDto.getPassword(), user.getPassword());

        if (userDto.getRole() != null) {
            builder.role(roleRepository.findByRoleName(userDto.getRole()).orElse(user.getRole()));
        }

// fixed values
        builder.accountNonLocked(false)
                .accountNonExpired(true)
                .enabled(true)
                .credentialsExpiryDate(LocalDate.now().plusYears(1))
                .accountExpiryDate(LocalDate.now().plusYears(1))
                .isTwoFactorEnabled(false)
                .signUpMethod("email");

        User  newUser = userRepository.save(builder.build());
        cachingService.set(newUser.getUserName(), UserMapper.toDto(newUser));


        return UserDto.buildResponse(userDto);

    }

    @Override
    @Transactional
    public Boolean deleteUser(String userName) {
      User user =   findByUserName(userName)
                .orElseThrow( () -> new RuntimeException("User not found with username: " + userName));

      cachingService.delete(user.getUserName());
      userRepository.delete(user);

      return true;
    }


    private <T> void setIfNotNull(Consumer<T> setter, T newValue,T existingValue ) {
        if (newValue != null) {
            setter.accept(newValue);
        }else {
            setter.accept(existingValue);
        }
    }
}
