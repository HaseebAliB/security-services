package com.sercurity.services.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .formLogin(form -> form.successHandler((request, response, authentication) -> {
                    SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
                    if (savedRequest != null) {
                        System.out.println("✅ Saved request URL: " + savedRequest.getRedirectUrl());
                        response.sendRedirect(savedRequest.getRedirectUrl());
                    } else {
                        System.out.println("⚠️ No saved request found, redirecting to default /hello");
                        response.sendRedirect("/hello");
                    }
                }));
      //  http.sessionManagement(session ->
        //        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //http.httpBasic(withDefaults());
        return http.build();
    }

}
