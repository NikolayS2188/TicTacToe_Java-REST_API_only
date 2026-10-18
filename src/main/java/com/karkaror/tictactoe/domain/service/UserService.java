package com.karkaror.tictactoe.domain.service;

import com.karkaror.tictactoe.domain.model.User;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 * Определяет операции управления пользователями.
 */
public interface UserService {
    /**
     * Регистрирует нового пользователя.
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return зарегистрированный пользователь
     * @throws RuntimeException если пользователь уже существует
     */
    User register(String login, String password);

    /**
     * Находит пользователя по логину.
     * @param login логин пользователя
     * @return Optional с пользователем
     */
    Optional<User> findByLogin(String login);

    /**
     * Находит пользователя по UUID.
     * @param id идентификатор пользователя
     * @return Optional с пользователем
     */
    Optional<User> findById(UUID id);

    /**
     * Проверяет существование пользователя с указанным логином.
     * @param login логин для проверки
     * @return true если пользователь существует
     */
    boolean existsByLogin(String login);
}
