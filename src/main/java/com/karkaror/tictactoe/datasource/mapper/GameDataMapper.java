package com.karkaror.tictactoe.datasource.mapper;

import com.karkaror.tictactoe.datasource.model.GameEntity;
import com.karkaror.tictactoe.datasource.model.GameFieldEntity;
import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.GameField;
import org.springframework.stereotype.Component;
import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Маппер для преобразования между доменной моделью Game и сущностью GameEntity.
 * Обеспечивает изоляцию слоев данных и доменной логики.
 */
@Component
public class GameDataMapper {

    /**
     * Преобразует доменную модель в сущность для сохранения в БД.
     * @param game доменная модель игры
     * @return сущность для сохранения
     */
    public GameEntity toEntity(Game game) {
        GameField domainField = game.getCopyField();
        GameFieldEntity fieldEntity = new GameFieldEntity(copyMatrix(domainField.getMatrix()));

        return new GameEntity(
                game.getId(),
                game.getCreatedAt(),
                fieldEntity,
                game.getStatus().name(),
                game.getPlayer1Id(),
                game.getPlayer2Id(),
                game.getCurrentPlayerId(),
                game.getWinnerId(),
                game.isGameOver(),
                game.getWinner(),
                game.isVsComputer()
        );
    }

    /**
     * Преобразует сущность из БД в доменную модель.
     * @param entity сущность из БД
     * @return доменная модель игры
     */
    public Game toDomain(GameEntity entity) {
        GameFieldEntity fieldEntity = entity.getField();
        Integer[][] matrix = fieldEntity.getMatrix();

        GameField domainField = new GameField();
        if (matrix != null) {
            for (int i = 0; i < Math.min(matrix.length, ROWS); i++) {
                for (int j = 0; j < Math.min(matrix[i].length, COLUMNS); j++) {
                    if (matrix[i][j] != null) {
                        domainField.setPosition(i, j, matrix[i][j]);
                    }
                }
            }
        }

        return new Game(
                entity.getId(),
                entity.getCreatedAt(),
                domainField,
                Game.GameStatus.valueOf(entity.getStatus()),
                entity.getPlayer1Id(),
                entity.getPlayer2Id(),
                entity.getCurrentPlayerId(),
                entity.getWinnerId(),
                PLAYER1,
                PLAYER2,
                entity.isVsComputer()
        );
    }

    /**
     * Создает глубокую копию матрицы для обеспечения иммутабельности.
     * @param source исходная матрица
     * @return копия матрицы
     */
    private Integer[][] copyMatrix(Integer[][] source) {
        if (source == null) {
            return new Integer[ROWS][COLUMNS];
        }
        Integer[][] copy = new Integer[ROWS][COLUMNS];
        for (int i = 0; i < Math.min(source.length, ROWS); i++) {
            System.arraycopy(source[i], 0, copy[i], 0, Math.min(source[i].length, COLUMNS));
        }
        return copy;
    }
}
