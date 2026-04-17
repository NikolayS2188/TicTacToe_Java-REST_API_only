package com.karkaror.tictactoe.domain.model;

import com.karkaror.tictactoe.domain.util.GameRules;
import java.util.UUID;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Модель текущей игры с уникальным id.
 * Объекты иммутабельны: после создания все поля класса неизменны, для нового хода создается новый экземпляр.
 * Для совершения хода создаётся новый экземпляр с помощью метода withMove().
 */
public class Game {
    private final UUID id;                // уникальный идентификатор игры
    private final GameField field;        // игровое поле 3x3: 0 - пусто, 1 - игрок, 2 - компьютер
    private final boolean gameOver;       // флаг окончания игры
    private final int winner;             // победитель: 0 - ничья/нет, 1 - игрок, 2 - компьютер

    /**
     * Конструктор для начала игры - все клетки игрового поля пустые.
     */
    public Game(UUID id) {
        this(id, new GameField(), false, GAME_CONTINUES);
    }

    /**
     * Конструктор продолжающейся игры - создается копия игрового поля.
     */
    public Game(UUID id, GameField field) {
        this.id = id;
        this.field = field.copy();
        int winner = GameRules.check3x3BoardWinner(field.getMatrix());
        this.gameOver = (winner != GAME_CONTINUES) || GameRules.isBoardFull(field.getMatrix());
        this.winner = winner;
    }

    /**
     * Конструктор для использования внутри класса - в начале игры известны gameOver, winner.
     */
    public Game(UUID id, GameField field, boolean gameOver, int winner) {
        this.id = id;
        this.field = field.copy();
        this.gameOver = gameOver;
        this.winner = winner;
    }

    public UUID getId() { return id; }

    /**
     * Возвращает копию игрового поля для предотвращения изменений извне.
     */
    public GameField getCopyField() {
        return field.copy(); // Возвращаем копию для иммутабельности
    }

    public boolean isGameOver() { return gameOver; }

    public int getWinner() { return winner; }

    /**
     * Создает новый объект текущей игры, где ход игрока фиксируется в копию игрового поля.
     * @param row строка (0-2)
     * @param col столбец (0-2)
     * @param playerId идентификатор игрока (1 или 2)
     * @return новый экземпляр Game
     * @throws IllegalArgumentException если координаты вне диапазона
     * @throws IllegalStateException    если клетка уже занята
     */
    public Game withMove(int row, int col, int playerId) {
        if (!field.isTruePosition(row, col))    {
            throw new IllegalArgumentException("Invalid row or column");
        }
        if (field.getPosition(row, col) != EMPTY) {
            throw new IllegalStateException("Cell is not empty");
        }
        GameField newField = field.copy();
        newField.setPosition(row, col, playerId);
        return new Game(id, newField);
    }
}