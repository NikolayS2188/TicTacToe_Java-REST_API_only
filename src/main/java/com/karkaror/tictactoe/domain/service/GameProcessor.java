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
     * Проверяет, завершена ли игра.
     * @param game состояние игры
     * @return true если игра окончена
     */
    boolean isGameOver(Game game);
}
