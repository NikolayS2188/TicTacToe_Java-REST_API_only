package com.karkaror.tictactoe.datasource.model;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import jakarta.persistence.*;
import java.util.UUID;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Сущность для хранения поля игры в базе данных.
 */
@Entity
@Table(name = "game_fields")
public class GameFieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Integer[][] matrix;

    public GameFieldEntity() {
        this.matrix = new Integer[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                this.matrix[i][j] = EMPTY;
            }
        }
    }

    public GameFieldEntity(Integer[][] matrix) {
        this.matrix = matrix;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Integer[][] getMatrix() { return matrix; }
    public void setMatrix(Integer[][] matrix) { this.matrix = matrix; }

    public Integer getPosition(int row, int col) { return matrix[row][col]; }
    public void setPosition(int row, int col, int value) { matrix[row][col] = value; }
}
