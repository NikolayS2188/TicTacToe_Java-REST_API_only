package com.karkaror.tictactoe.domain.service;

import com.karkaror.tictactoe.domain.model.User;
import com.karkaror.tictactoe.web.model.JwtRequest;
import com.karkaror.tictactoe.web.model.JwtResponse;

/**
 * Сервис для аутентификации и авторизации.
 * Обеспечивает регистрацию, вход в систему и обновление токенов.
 */
public interface AuthService {
    /**
     * Регистрирует нового пользователя.
     * @param login логин
     * @param password пароль
     * @return зарегистрированный пользователь
     */
    User register(String login, String password);

    /**
     * Аутентифицирует пользователя по логину и паролю.
     * При успешной проверке выдаёт пару JWT-токенов:
     * access-токен (короткоживущий) и refresh-токен (долгоживущий).
     *
     * @param request запрос с логином и паролем
     * @return пара токенов (access + refresh)
     * @throws RuntimeException если пользователь не найден
     *                          или пароль неверный
     */
    JwtResponse login(JwtRequest request);

    /**
     * Обновляет access-токен по действующему refresh-токену.
     * Refresh-токен остаётся тем же — продлевается только access-токен.
     *
     * @param refreshToken действующий refresh-токен
     * @return новый access-токен и тот же refresh-токен
     * @throws RuntimeException если refresh-токен невалиден
     *                          или пользователь не найден
     */
    JwtResponse refreshAccessToken(String refreshToken);

    /**
     * Обновляет refresh-токен по действующему refresh-токену.
     * Выдаёт оба токена заново — access и refresh.
     *
     * @param refreshToken действующий refresh-токен
     * @return новые access- и refresh-токены
     * @throws RuntimeException если refresh-токен невалиден
     *                          или пользователь не найден
     */
    JwtResponse refreshRefreshToken(String refreshToken);

    /**
     * Создаёт объект аутентификации (JwtAuthentication) по access-токену.
     * Используется в AuthFilter для установки аутентификации в SecurityContext.
     *
     * @param accessToken действующий access-токен
     * @return объект аутентификации с UUID пользователя и его ролями
     * @throws RuntimeException если access-токен невалиден
     */
    JwtAuthentication getAuthentication(String accessToken);
}
