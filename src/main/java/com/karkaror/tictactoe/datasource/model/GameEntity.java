package com.karkaror.tictactoe.datasource.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Сущность для хранения игры в базе данных
 * с отсутствием бизнес-логики,
 * с наличием JPA аннотаций для ORM.
 */
@Entity
@Table(name = "games")
public class GameEntity {
    @Id
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "field_id", referencedColumnName = "id")
    private GameFieldEntity field;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "player1_id", nullable = false)
    private UUID player1Id;

    @Column(name = "player2_id")
    private UUID player2Id;

    @Column(name = "current_player_id")
    private UUID currentPlayerId;

    @Column(name = "winner_id")
    private UUID winnerId;

    @Column(name = "game_over", nullable = false)
    private boolean gameOver;

    @Column(name = "winner", nullable = false)
    private int winner;

    @Column(name = "vs_computer", nullable = false)
    private boolean vsComputer;

    public GameEntity() {}

    public GameEntity(UUID id, Instant createdAt, GameFieldEntity field, String status, UUID player1Id,
                      UUID player2Id, UUID currentPlayerId, UUID winnerId,
                      boolean gameOver, int winner, boolean vsComputer) {
        this.id = id;
        this.createdAt = createdAt;
        this.field = field;
        this.status = status;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.currentPlayerId = currentPlayerId;
        this.winnerId = winnerId;
        this.gameOver = gameOver;
        this.winner = winner;
        this.vsComputer = vsComputer;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public GameFieldEntity getField() { return field; }
    public void setField(GameFieldEntity field) { this.field = field; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public UUID getPlayer1Id() { return player1Id; }
    public void setPlayer1Id(UUID player1Id) { this.player1Id = player1Id; }

    public UUID getPlayer2Id() { return player2Id; }
    public void setPlayer2Id(UUID player2Id) { this.player2Id = player2Id; }

    public UUID getCurrentPlayerId() { return currentPlayerId; }
    public void setCurrentPlayerId(UUID currentPlayerId) { this.currentPlayerId = currentPlayerId; }

    public UUID getWinnerId() { return winnerId; }
    public void setWinnerId(UUID winnerId) { this.winnerId = winnerId; }

    public boolean isGameOver() { return gameOver; }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }

    public int getWinner() { return winner; }
    public void setWinner(int winner) { this.winner = winner; }

    public boolean isVsComputer() { return vsComputer; }
    public void setVsComputer(boolean vsComputer) { this.vsComputer = vsComputer; }
}
