package com.karkaror.tictactoe.datasource.mapper;

import com.karkaror.tictactoe.datasource.model.UserEntity;
import com.karkaror.tictactoe.domain.model.User;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между доменной моделью User и сущностью UserEntity.
 */
@Component
public class UserDataMapper {

    /**
     * Преобразует доменную модель пользователя в сущность для БД.
     * @param user доменная модель пользователя
     * @return сущность для сохранения
     */
    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.password(),
                user.roles()
        );
    }

    /**
     * Преобразует сущность из БД в доменную модель пользователя.
     * @param entity сущность из БД
     * @return доменная модель пользователя
     */
    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getLogin(),
                entity.getPassword(),
                entity.getRoles()
        );
    }
}
