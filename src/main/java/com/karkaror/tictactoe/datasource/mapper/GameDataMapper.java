package com.karkaror.tictactoe.datasource.mapper;

import com.karkaror.tictactoe.datasource.model.GameFieldEntity;
import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.datasource.model.GameEntity;
import com.karkaror.tictactoe.domain.model.GameField;

import static com.karkaror.tictactoe.domain.common.DomainConstants.COLUMNS;
import static com.karkaror.tictactoe.domain.common.DomainConstants.ROWS;

/**
 * Маппер для преобразования между доменной моделью Game и сущностью GameEntity.
 */
public class GameDataMapper {

    /**
     * Преобразует доменную модель в сущность для хранения.
     */
    public GameEntity toEntity(Game game) {
        GameField domainField = game.getCopyField();
        GameFieldEntity fieldEntity = new GameFieldEntity(domainField.getMatrix());

        return new GameEntity(
                game.getId(),
                fieldEntity,
                game.isGameOver(),
                game.getWinner()
        );
    }

    /**
     * Преобразует сущность в доменную модель.
     */
    public Game toDomain(GameEntity entity) {
        GameFieldEntity fieldEntity = entity.getField();
        GameField domainField = new GameField();

        // Копируем матрицу
        Integer[][] matrix = fieldEntity.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != null) {
                    domainField.setPosition(i, j, matrix[i][j]);
                }
            }
        }

        return new Game(
                entity.getId(),
                domainField,
                entity.isGameOver(),
                entity.getWinner()
        );
    }
}