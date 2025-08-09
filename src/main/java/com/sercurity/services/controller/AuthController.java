package com.sercurity.services.controller;

import com.sercurity.services.config.AuthManager;
import com.sercurity.services.dtos.LoginRequest;
import com.sercurity.services.dtos.LoginResponse;
import com.sercurity.services.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class AuthController {

    private final AuthManager authManager;
    private final JwtUtils jwtUtils;
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authManager.signin(loginRequest);
            if (response == null) {
                return ResponseEntity.status(401).body("Signin failed: Invalid credentials");
            }else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Signin failed: " + e.getMessage());
        }

    }

    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @GetMapping("/oauth/success")
    public String oAuthSuccess(@RequestParam("token") String token){
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        return "OAuth Success! Welcome: " + userName;
    }



}
