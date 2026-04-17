package com.karkaror.tictactoe.domain.model;

import java.util.Arrays;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Модель игрового поля в виде целочисленной матрицы.
 * Объекты иммутабельны, после нового хода создается новый экземпляр игрового поля.
 */
public class GameField {
    final private Integer[][] matrix;

    public GameField() {
        this.matrix = new Integer[ROWS][COLUMNS];
        // Инициализируем все клетки нулями
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                this.matrix[i][j] = EMPTY;
            }
        }
    }

    public Integer[][] getMatrix() {
        return this.matrix;
    }

    public int getPosition(int row, int col) { return matrix[row][col]; }

    public void setPosition(int row, int col, int sign) {
        if (isTruePosition(row, col) && isTrueSign(sign)) {
            matrix[row][col] = sign;
        }
    }

    public boolean isTruePosition(int row, int col) {
        return 0 <= row && row < ROWS &&
                0 <= col && col < COLUMNS;
    }

    public boolean isTrueSign(int sign) {
        return sign == PLAYER1 || sign == PLAYER2 || sign == EMPTY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) { return false; }
        GameField gameField = (GameField) o;
        return Arrays.deepEquals(this.matrix, gameField.matrix);
    }

    @Override
    public int hashCode() { return Arrays.deepHashCode(matrix); }

    /**
     * Копирование матрицы.
     */
    public GameField copy() {
        GameField copy = new GameField();
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(this.matrix[i], 0, copy.matrix[i], 0, COLUMNS);
        }
        return copy;
    }

    /**
     * Проверка на пустоту.
     */
    public boolean isEmpty() {
        for (Integer[] row : matrix) {
            for (Integer cell : row) {
                if (cell != null && cell != EMPTY) return false;
            }
        }
        return true;
    }

    /**
     * Подсчет пустых клеток.
     */
    public int getEmptyCount() {
        int count = 0;
        for (Integer[] row : matrix) {
            for (Integer cell : row) {
                if (cell == null || cell == EMPTY) count++;
            }
        }
        return count;
    }
}
