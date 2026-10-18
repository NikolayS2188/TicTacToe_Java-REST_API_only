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
     * @param domainField доменное поле
     * @return веб-поле
     */
    public GameFieldWeb toWebField(GameField domainField) {
        return new GameFieldWeb(domainField.getMatrix());
    }

    /**
     * Преобразует доменную игру в ответ.
     * @param domainGame доменная игра
     * @return ответ с состоянием игры
     */
    public GameStateResponse toResponse(Game domainGame) {
        if (domainGame == null) {
            return new GameStateResponse(null, null, "ERROR", null, null, null);
        }
        return new GameStateResponse(
                domainGame.getId(),
                toWebField(domainGame.getCopyField()),
                domainGame.getStatus().name(),
                domainGame.getCurrentPlayerId(),
                domainGame.getWinnerId(),
                null
        );
    }

    /**
     * Преобразует доменную игру в ответ с сообщением.
     * @param domainGame доменная игра
     * @param message сообщение
     * @return ответ с состоянием игры и сообщением
     */
    public GameStateResponse toResponse(Game domainGame, String message) {
        if (domainGame == null) {
            return new GameStateResponse(null, null, "ERROR", null, null, message);
        }
        return new GameStateResponse(
                domainGame.getId(),
                toWebField(domainGame.getCopyField()),
                domainGame.getStatus().name(),
                domainGame.getCurrentPlayerId(),
                domainGame.getWinnerId(),
                message
        );
    }
}
