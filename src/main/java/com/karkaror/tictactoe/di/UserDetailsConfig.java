package com.karkaror.tictactoe.di;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Фиктивный UserDetailsService.
 * Нужен, чтобы Spring Boot не генерировал пароль по умолчанию.
 * Аутентификация выполняется через JWT (AuthFilter + JwtProvider).
 */
@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("JWT authentication is used");
        };
    }
}
