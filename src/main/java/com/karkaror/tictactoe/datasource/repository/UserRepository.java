package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.datasource.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA репозиторий для работы с пользователями в базе данных.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {
    /**
     * Находит пользователя по логину.
     * @param login логин пользователя
     * @return Optional с пользователем, если найден
     */
    Optional<UserEntity> findByLogin(String login);

    /**
     * Проверяет существование пользователя с указанным логином.
     * @param login логин для проверки
     * @return true если пользователь существует
     */
    boolean existsByLogin(String login);
}

