package com.karkaror.tictactoe.domain.service;

import com.karkaror.tictactoe.domain.model.Game;

/**
 * Интерфейс, определяющий операции логики игры.
 */
public interface GameProcessor {
    /**
     * Выполняет ход компьютера на основе текущего состояния игры.
     * @param game текущее состояние
     * @return новое состояние игры после хода компьютера
     */
    Game makeComputerMove(Game game);

    /**
     * Проверяет, допустим ли ход игрока.
     * @param game текущее состояние игры
     * @param row строка хода
     * @param col столбец хода
     * @param playerId идентификатор игрока (ожидается 1)
     * @return true если ход допустим (клетка пуста и игра не окончена)
     */
    boolean isValidMove(Game game, int row, int col, int playerId);

    /**
     * Проверяет, завершена ли игра.
     * @param game состояние игры
     * @return true если игра окончена
     */
    boolean isGameOver(Game game);
}