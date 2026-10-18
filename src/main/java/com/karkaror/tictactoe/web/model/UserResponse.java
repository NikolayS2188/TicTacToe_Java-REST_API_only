package com.karkaror.tictactoe.web.model;

import java.util.UUID;

/**
 * DTO для ответа с информацией о пользователе.
 */
public class UserResponse {
    private final UUID userId;
    private final String login;

    public UserResponse(UUID userId, String login) {
        this.userId = userId;
        this.login = login;
    }

    public UUID getUserId() { return userId; }
    public String getLogin() { return login; }
}
