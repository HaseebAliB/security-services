package com.sercurity.services.controller;

import com.sercurity.services.dtos.LoginRequest;
import com.sercurity.services.dtos.LoginResponse;
import com.sercurity.services.services.UserService;
import com.sercurity.services.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    @GetMapping("/hello")
    public String showMsg(){
        return "hello!";
    }


    @PostMapping("/public/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse  response = userService.signin(loginRequest);
            if (response == null) {
                return ResponseEntity.status(401).body("Signin failed: Invalid credentials");
            }else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Signin failed: " + e.getMessage());
        }

    }


    @GetMapping("/oauth/success")
    public String oAuthSuccess(@RequestParam("token") String token){
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        return "OAuth Success! Welcome: " + userName;
    }


}