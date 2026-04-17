package com.karkaror.tictactoe.web.model;

import java.util.UUID;

/**
 * DTO для ответа о полном состоянии игры в веб-слое.
 * @param gameId  идентификатор игры
 * @param field   игровое поле 3x3 (0 - пусто, 1 - игрок, 2 - компьютер)
 * @param status  статус игры: IN_PROGRESS, WIN_PLAYER, WIN_COMPUTER, DRAW
 * @param message дополнительное сообщение (может быть null)
 */
public record GameStateResponse(
        UUID gameId,
        GameFieldWeb field,   // текущее игровое поле
        String status,        // "IN_PROGRESS", "WIN_PLAYER", "WIN_COMPUTER", "DRAW"
        String message        // дополнительное сообщение (например, описание ошибки или информации)
) {}