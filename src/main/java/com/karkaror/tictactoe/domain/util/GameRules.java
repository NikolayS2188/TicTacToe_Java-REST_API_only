package com.karkaror.tictactoe.domain.util;

import java.util.Objects;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Утилитарный класс с методами для проверки состояния игры.
 * Содержит чистую логику, не зависящую от состояния.
 */
public final class GameRules {
    private GameRules() {}

    /**
     * Проверяет для игрового поля 3x3 по строкам, столбцам и диагоналям, есть ли победитель.
     * @param matrix матрица 3x3
     * @return 0 - нет победителя, 1 - игрок, 2 - компьютер
     */
    public static int check3x3BoardWinner(Integer[][] matrix) {
        // Проверка строк
        for (int i = 0; i < ROWS; i++) {
            if (matrix[i][0] != EMPTY &&
                    Objects.equals(matrix[i][0], matrix[i][1]) &&
                    Objects.equals(matrix[i][1], matrix[i][2])) {
                return matrix[i][0];
            }
        }
        // Проверка столбцов
        for (int i = 0; i < COLUMNS; i++) {
            if (matrix[0][i] != EMPTY &&
                    Objects.equals(matrix[0][i], matrix[1][i]) &&
                    Objects.equals(matrix[1][i], matrix[2][i])) {
                return matrix[0][i];
            }
        }
        // Проверка диагоналей
        if (matrix[0][0] != EMPTY &&
                Objects.equals(matrix[0][0], matrix[1][1]) &&
                Objects.equals(matrix[1][1], matrix[2][2])) {
            return matrix[0][0];
        }
        if (matrix[0][2] != EMPTY &&
                Objects.equals(matrix[0][2], matrix[1][1]) &&
                Objects.equals(matrix[1][1], matrix[2][0])) {
            return matrix[0][2];
        }
        return GAME_CONTINUES;
    }

    /**
     * Проверяет, заполнена ли матрица полностью.
     */
    public static boolean isBoardFull(Integer[][] matrix) {
        for (Integer[] row : matrix) {
            for (Integer cell : row) {
                if (cell == null || cell == EMPTY) return false;
            }
        }
        return true;
    }
}
