package com.karkaror.tictactoe.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

/**
 * DTO для игрового поля в веб-слое.
 * Содержит только данные, без логики.
 */
public class GameFieldWeb {
    private final Integer[][] matrix;

    @JsonCreator
    public GameFieldWeb(@JsonProperty("matrix") Integer[][] matrix) {
        this.matrix = matrix != null ? matrix : new Integer[3][3];
    }

    public Integer[][] getMatrix() {
        // Возвращаем копию для неизменяемости
        Integer[][] copy = new Integer[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, 3);
        }
        return copy;
    }

    public Integer getPosition(int row, int col) {
        return matrix[row][col];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameFieldWeb that = (GameFieldWeb) o;
        return Arrays.deepEquals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(matrix);
    }
}
