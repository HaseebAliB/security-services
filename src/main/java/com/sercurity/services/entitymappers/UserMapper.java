package com.sercurity.services.entitymappers;

import com.sercurity.services.dtos.UserDto;
import com.sercurity.services.models.Role;
import com.sercurity.services.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public static UserDto toDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().getRoleName() : null)
                .build();
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        if (dto.getRole() != null) {
            user.setRole(new Role(dto.getRole()));
        }
        return user;
    }

}
