package com.karkaror.tictactoe.web.model;

/**
 * DTO для ответа с токенами.
 * @param type         тип токена "Bearer" - стандартная схема авторизации
 *                     из RFC 6750 (OAuth 2.0: Bearer Token Usage)
 *                     для передачи токена в заголовке Authorization.
 * @param accessToken  короткоживущий токен для доступа.
 * @param refreshToken долгоживущий токен для обновления.
 */
public record JwtResponse(
        String type,
        String accessToken,
        String refreshToken
) {
    public JwtResponse(String accessToken, String refreshToken) {
        this("Bearer", accessToken, refreshToken);
    }
}
