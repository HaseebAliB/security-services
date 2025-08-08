package com.sercurity.services.services;

import com.sercurity.services.dtos.LoginRequest;
import com.sercurity.services.dtos.LoginResponse;
import com.sercurity.services.models.User;

import java.util.Optional;

public interface UserService {
LoginResponse signin(LoginRequest loginRequest) throws Exception;
    Optional<User> findByUserName(String username) ;
    User registerUser(User user);

}
