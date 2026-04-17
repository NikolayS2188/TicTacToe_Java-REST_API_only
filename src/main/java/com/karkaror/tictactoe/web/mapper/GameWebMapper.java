package com.karkaror.tictactoe.web.mapper;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.GameField;
import com.karkaror.tictactoe.web.model.GameFieldWeb;
import com.karkaror.tictactoe.web.model.GameStateResponse;
import org.springframework.stereotype.Component;

/**
 * Маппер для преобразования между доменными моделями (Game, GameField) и веб-DTO (GameStateResponse, GameFieldWeb).
 */
@Component
public class GameWebMapper {

    /**
     * Преобразует доменное поле в веб-поле.
     */
    public GameFieldWeb toWebField(GameField domainField) {
        return new GameFieldWeb(domainField.getMatrix());
    }

    /**
     * Преобразует веб-поле в доменное поле.
     */
    public GameField toDomainField(GameFieldWeb webField) {
        GameField field = new GameField();
        Integer[][] matrix = webField.getMatrix();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != null) {
                    field.setPosition(i, j, matrix[i][j]);
                }
            }
        }
        return field;
    }

    /**
     * Преобразует доменную игру в веб-игру.
     */
    public GameStateResponse toWebGame(Game domainGame) {
        return new GameStateResponse(
                domainGame.getId(),
                toWebField(domainGame.getCopyField()),
                getStatusString(domainGame),
                null
        );
    }

    /**
     * Преобразует объект Game в DTO ответа.
     * @param domainGame доменная модель игры
     * @return DTO для отправки клиенту
     */
    public GameStateResponse toResponse(Game domainGame) {
        return new GameStateResponse(
                domainGame.getId(),
                toWebField(domainGame.getCopyField()),
                getStatusString(domainGame),
                null
        );
    }

    private String getStatusString(Game game) {
        if (!game.isGameOver()) {
            return "IN_PROGRESS";
        }
        int winner = game.getWinner();
        if (winner == 1) return "WIN_PLAYER";
        if (winner == 2) return "WIN_COMPUTER";
        return "DRAW";
    }
}