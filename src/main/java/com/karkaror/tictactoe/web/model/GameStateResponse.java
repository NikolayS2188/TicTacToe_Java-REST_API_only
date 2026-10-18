package com.karkaror.tictactoe.web.model;

import java.util.UUID;


/**
 * DTO для ответа о полном состоянии игры в веб-слое.
 * @param gameId          идентификатор игры
 * @param field           игровое поле 3x3 (0 - пусто, 1 - игрок, 2 - компьютер)
 * @param status          статус игры: WAITING_FOR_PLAYERS, IN_PROGRESS, DRAW, FINISHED
 * @param currentPlayerId UUID игрока, чей сейчас ход
 * @param winnerId        UUID победителя (null если нет победителя)
 * @param message         дополнительное сообщение (например, описание ошибки или информации. Может быть null)
 */
public record GameStateResponse(
        UUID gameId,
        GameFieldWeb field,
        String status,
        UUID currentPlayerId,
        UUID winnerId,
        String message
) {}
