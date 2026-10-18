package com.karkaror.tictactoe.web.controller;

import com.karkaror.tictactoe.domain.model.User;
import com.karkaror.tictactoe.domain.service.AuthService;
import com.karkaror.tictactoe.domain.service.UserService;
import com.karkaror.tictactoe.web.model.ErrorResponse;
import com.karkaror.tictactoe.web.model.JwtRequest;
import com.karkaror.tictactoe.web.model.JwtResponse;
import com.karkaror.tictactoe.web.model.RefreshJwtRequest;
import com.karkaror.tictactoe.web.model.SignUpRequest;
import com.karkaror.tictactoe.web.model.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

/**
 * REST-контроллер для аутентификации и регистрации пользователей.
 * Обрабатывает запросы на /auth/register, /auth/login,
 * /auth/refresh/access, /auth/refresh/refresh, /auth/user/me.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Регистрация нового пользователя.
     * @param request запрос с логином и паролем
     * @return ResponseEntity с данными пользователя (201 Created)
     *         или ошибкой (409 Conflict)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpRequest request) {
        try {
            User user = authService.register(request.getLogin(), request.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new UserResponse(user.id(), user.login()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("REGISTRATION_FAILED", e.getMessage(), 409));
        }
    }

    /**
     * Логин пользователя.
     * @param request запрос с логином и паролем
     * @return ResponseEntity с парой JWT-токенов (200 OK)
     *         или ошибкой (401 Unauthorized)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody JwtRequest request) {
        try {
            JwtResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("AUTHENTICATION_FAILED", e.getMessage(), 401));
        }
    }

    /**
     * Обновление access-токена по действующему refresh-токену.
     * Refresh-токен остаётся тем же — продлевается только access-токен.
     * @param request запрос с refresh-токеном
     * @return ResponseEntity с новой парой токенов (200 OK)
     *         или ошибкой (401 Unauthorized)
     */
    @PostMapping("/refresh/access")
    public ResponseEntity<?> refreshAccess(@Valid @RequestBody RefreshJwtRequest request) {
        try {
            JwtResponse response = authService.refreshAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("REFRESH_FAILED", e.getMessage(), 401));
        }
    }

    /**
     * Обновление refresh-токена по действующему refresh-токену.
     * Выдаёт оба токена заново — access и refresh.
     * @param request запрос с refresh-токеном
     * @return ResponseEntity с новой парой токенов (200 OK)
     *         или ошибкой (401 Unauthorized)
     */
    @PostMapping("/refresh/refresh")
    public ResponseEntity<?> refreshRefresh(@Valid @RequestBody RefreshJwtRequest request) {
        try {
            JwtResponse response = authService.refreshRefreshToken(request.getRefreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("REFRESH_FAILED", e.getMessage(), 401));
        }
    }

    /**
     * Получение информации о текущем пользователе по access-токену.
     * UUID пользователя извлекается из токена — не требует передачи в URL.
     * @param userId UUID текущего пользователя (из SecurityContext)
     * @return ResponseEntity с данными пользователя (200 OK)
     *         или ошибкой (404 Not Found — пользователь с UUID из токена не найден в БД,
     *                      401 Unauthorized — аутентификация отсутствует или не завершена)
     */
    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("UNAUTHORIZED", "Not authenticated", 401));
        }

        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("USER_NOT_FOUND", "User not found", 404));
        }

        User user = userOptional.get();
        return ResponseEntity.ok(new UserResponse(user.id(), user.login()));
    }
}
