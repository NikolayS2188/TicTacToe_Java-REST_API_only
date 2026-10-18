package com.karkaror.tictactoe.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO для запроса хода от клиента.
 * @param row номер строки (0-2)
 * @param col номер столбца (0-2)
 */
public record GameMoveRequest(int row, int col) {
    @JsonCreator
    public GameMoveRequest(@JsonProperty("row") int row, @JsonProperty("col") int col) {
        this.row = row;
        this.col = col;
    }
}
