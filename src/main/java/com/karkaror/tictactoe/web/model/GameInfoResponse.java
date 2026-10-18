package com.karkaror.tictactoe.web.model;

import java.util.UUID;

/**
 * DTO для краткой информации об игре.
 * Используется при получении списка доступных игр.
 */
public class GameInfoResponse {
    private final UUID gameId;
    private final UUID player1Id;
    private final UUID player2Id;
    private final String status;
    private final boolean vsComputer;

    public GameInfoResponse(UUID gameId, UUID player1Id, UUID player2Id,
                            String status, boolean vsComputer) {
        this.gameId = gameId;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.status = status;
        this.vsComputer = vsComputer;
    }

    public UUID getGameId() { return gameId; }
    public UUID getPlayer1Id() { return player1Id; }
    public UUID getPlayer2Id() { return player2Id; }
    public String getStatus() { return status; }
    public boolean isVsComputer() { return vsComputer; }
}
