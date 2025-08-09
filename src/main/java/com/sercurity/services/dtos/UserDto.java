package com.sercurity.services.dtos;

import com.sercurity.services.models.AppRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private AppRole role;

    public static UserResponseDto buildResponse(UserDto userDto) { //to exclude password in api response
        return UserResponseDto.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .build();
    }
}
