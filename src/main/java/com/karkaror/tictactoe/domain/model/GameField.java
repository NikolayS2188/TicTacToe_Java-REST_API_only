package com.karkaror.tictactoe.domain.model;

import java.util.Arrays;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Игровое поле в виде целочисленной матрицы с защитой от внешних изменений.
 * Внутренняя матрица может изменяться только через методы класса,
 * а для внешнего доступа всегда возвращается копия через copy().
 * Т.о. объекты иммутабельны, после нового хода создается новый экземпляр игрового поля.
 */
public class GameField {
    final private Integer[][] matrix;

    public GameField() {
        this.matrix = new Integer[ROWS][COLUMNS];
        // Инициализируем нулями
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                this.matrix[i][j] = EMPTY;
            }
        }
    }

    public Integer[][] getMatrix() {
        Integer[][] copy = new Integer[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(this.matrix[i], 0, copy[i], 0, COLUMNS);
        }
        return copy;
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
}
