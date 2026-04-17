package com.karkaror.tictactoe.datasource.model;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Сущность для хранения в хранилище состояния игрового поля.
 */
public class GameFieldEntity {
    private Integer[][] matrix;  // 3x3 матрица

    public GameFieldEntity() {
        this.matrix = new Integer[ROWS][COLUMNS];
        // Инициализируем нулями
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                this.matrix[i][j] = EMPTY;
            }
        }
    }

    public GameFieldEntity(Integer[][] matrix) {
        this.matrix = matrix;
    }

    public Integer[][] getMatrix() { return matrix; }
    public void setMatrix(Integer[][] matrix) { this.matrix = matrix; }

    public Integer getPosition(int row, int col) { return matrix[row][col]; }
    public void setPosition(int row, int col, int value) { matrix[row][col] = value; }
}