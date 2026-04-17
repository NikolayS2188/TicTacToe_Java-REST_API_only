package com.karkaror.tictactoe.datasource.model;

import java.util.UUID;

/**
 * Сущность для хранения в хранилище состояния игры.
 * Отличается от доменной модели отсутствием бизнес-логики.
 */
public class GameEntity {
    private UUID id;
    private GameFieldEntity field;
    private boolean gameOver;
    private int winner;

    public GameEntity() {}

    public GameEntity(UUID id, GameFieldEntity field, boolean gameOver, int winner) {
        this.id = id;
        this.field = field;
        this.gameOver = gameOver;
        this.winner = winner;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public GameFieldEntity getField() { return field; }
    public void setField(GameFieldEntity field) { this.field = field; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public int getWinner() { return winner; }
    public void setWinner(int winner) { this.winner = winner; }
}