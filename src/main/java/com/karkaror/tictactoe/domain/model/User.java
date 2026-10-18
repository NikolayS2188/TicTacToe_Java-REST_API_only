package com.karkaror.tictactoe.domain.model;

import java.util.List;
import java.util.UUID;

/**
 * Доменная модель пользователя.
 * Иммутабельный объект, содержащий учетные данные и роли.
 */
public record User (
    UUID id,
    String login,
    String password,
    List<Role> roles
) {
    public User {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty");
        }
    }
}
