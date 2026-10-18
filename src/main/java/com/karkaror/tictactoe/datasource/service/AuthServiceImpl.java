package com.karkaror.tictactoe.datasource.service;

import com.karkaror.tictactoe.domain.model.User;
import com.karkaror.tictactoe.domain.service.AuthService;
import com.karkaror.tictactoe.domain.service.JwtAuthentication;
import com.karkaror.tictactoe.domain.service.UserService;
import com.karkaror.tictactoe.web.model.JwtRequest;
import com.karkaror.tictactoe.web.model.JwtResponse;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Реализация сервиса аутентификации.
 * Проверяет учетные данные и управляет регистрацией.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserService userService,
                           PasswordEncoder passwordEncoder,
                           JwtProvider jwtProvider,
                           JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User register(String login, String password) {
        return userService.register(login, password);
    }

    @Override
    public JwtResponse login(JwtRequest request) {
        User user = userService.findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.password())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        UUID userId = jwtUtil.extractUserId(claims);

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtProvider.generateAccessToken(user);
        return new JwtResponse(newAccessToken, refreshToken);
    }

    @Override
    public JwtResponse refreshRefreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        Claims claims = jwtProvider.getRefreshClaims(refreshToken);
        UUID userId = jwtUtil.extractUserId(claims);

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);
        return new JwtResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public JwtAuthentication getAuthentication(String accessToken) {
        Claims claims = jwtProvider.getAccessClaims(accessToken);
        return jwtUtil.createAuthentication(claims);
    }
}
