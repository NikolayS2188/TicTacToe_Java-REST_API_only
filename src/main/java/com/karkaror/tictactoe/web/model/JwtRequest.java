package com.karkaror.tictactoe.web.model;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для запроса авторизации.
 * Содержит логин и пароль.
 */
public class JwtRequest {
    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;

    public JwtRequest() {}

    public JwtRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
