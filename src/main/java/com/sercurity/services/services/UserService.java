package com.sercurity.services.services;

import com.sercurity.services.dtos.UserDto;
import com.sercurity.services.dtos.UserResponseDto;
import com.sercurity.services.models.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUserName(String username) ;
    User registerUserFromOauth(User user);

    UserResponseDto createUser(UserDto userDto);

    UserResponseDto updateUser(UserDto userDto);

    Boolean deleteUser(String userName);
}
