package com.karkaror.tictactoe.web.model;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса обновления токена.
 * Содержит refreshToken.
 */
public class RefreshJwtRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    public RefreshJwtRequest() {}

    public RefreshJwtRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
