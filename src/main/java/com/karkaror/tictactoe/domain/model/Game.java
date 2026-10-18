package com.karkaror.tictactoe.domain.model;

import com.karkaror.tictactoe.domain.util.GameRules;

import java.time.Instant;
import java.util.UUID;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Модель текущей игры с уникальным id.
 * Объекты иммутабельны: после создания все поля класса неизменны,
 * для нового хода создается новый экземпляр.
 * Все изменения состояния создают новый объект через методы with*().
 * Для совершения хода создаётся новый экземпляр с помощью метода withMove().
 * Игра всегда находится в каком-то одном состоянии.
 */
public class Game {
    /** Возможные состояния игры
     * - WAITING_FOR_PLAYERS: ожидание подключения второго игрока
     * - IN_PROGRESS: игра активна, игроки ходят по очереди
     * - DRAW: ничья, все клетки заполнены
     * - FINISHED: есть победитель
     */
    public enum GameStatus {
        WAITING_FOR_PLAYERS,
        IN_PROGRESS,
        DRAW,
        FINISHED
    }

    /** Уникальный идентификатор игры */
    private final UUID id;

    /** Дата создания игры */
    private final Instant createdAt;

    /** Игровое поле 3x3 */
    private final GameField field;

    /** Текущий статус игры */
    private final GameStatus status;

    /** UUID первого игрока */
    private final UUID player1Id;

    /** UUID второго игрока (может быть null до подключения) */
    private final UUID player2Id;

    /** UUID игрока, чей сейчас ход */
    private final UUID currentPlayerId;

    /** UUID победителя (null если нет победителя) */
    private final UUID winnerId;

    /** Символ первого игрока (PLAYER1=1) */
    private final int player1Symbol;

    /** Символ второго игрока (PLAYER2=2) */
    private final int player2Symbol;

    /** Флаг игры против компьютера */
    private final boolean isVsComputer;

    /**
     * Конструктор для создания новой игры с одним игроком.
     * Игра находится в состоянии WAITING_FOR_PLAYERS до подключения второго игрока.
     *
     * @param id уникальный идентификатор игры
     * @param player1Id UUID первого игрока
     * @param isVsComputer true если игра против компьютера
     */
    public Game(UUID id, UUID player1Id, boolean isVsComputer) {
        this.id = id;
        this.createdAt = Instant.now();
        this.field = new GameField();
        this.status = GameStatus.WAITING_FOR_PLAYERS;
        this.player1Id = player1Id;
        this.player2Id = null;
        this.currentPlayerId = player1Id;
        this.winnerId = null;
        this.player1Symbol = PLAYER1;
        this.player2Symbol = PLAYER2;
        this.isVsComputer = isVsComputer;
    }

    /**
     * Полный конструктор для всех полей.
     * Используется мапперами для восстановления состояния из БД.
     */
    public Game(UUID id, Instant createdAt, GameField field, GameStatus status, UUID player1Id,
                UUID player2Id, UUID currentPlayerId, UUID winnerId,
                int player1Symbol, int player2Symbol, boolean isVsComputer) {
        this.id = id;
        this.createdAt = createdAt;
        this.field = field.copy();
        this.status = status;
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.currentPlayerId = currentPlayerId;
        this.winnerId = winnerId;
        this.player1Symbol = player1Symbol;
        this.player2Symbol = player2Symbol;
        this.isVsComputer = isVsComputer;
    }

    /**
     * Создает новое состояние игры с подключенным вторым игроком.
     */
    public Game withPlayerJoined(UUID player2Id) {
        if (this.player2Id != null) {
            throw new IllegalStateException("Game already has two players");
        }
        return new Game(
                this.id,
                this.createdAt,
                this.field,
                GameStatus.IN_PROGRESS,
                this.player1Id,
                player2Id,
                this.player1Id,
                null,
                this.player1Symbol,
                this.player2Symbol,
                this.isVsComputer
        );
    }

    /**
     * Выполняет ход игрока и создает новое состояние игры.
     */
    public Game withMove(int row, int col, UUID playerId) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new IllegalStateException("Game is not in progress");
        }
        if (!playerId.equals(currentPlayerId)) {
            throw new IllegalStateException("Not your turn");
        }
        if (field.getPosition(row, col) != EMPTY) {
            throw new IllegalStateException("Cell is already occupied");
        }

        int playerSymbol = playerId.equals(player1Id) ? player1Symbol : player2Symbol;
        GameField newField = field.copy();
        newField.setPosition(row, col, playerSymbol);

        int winner = GameRules.check3x3BoardWinner(newField.getMatrix());
        boolean boardFull = GameRules.isBoardFull(newField.getMatrix());

        UUID nextPlayerId = playerId.equals(player1Id) ? player2Id : player1Id;

        if (winner == player1Symbol) {
            return new Game(id, createdAt, newField, GameStatus.FINISHED, player1Id, player2Id,
                    null, player1Id, player1Symbol, player2Symbol, isVsComputer);
        } else if (winner == player2Symbol) {
            return new Game(id, createdAt, newField, GameStatus.FINISHED, player1Id, player2Id,
                    null, player2Id, player1Symbol, player2Symbol, isVsComputer);
        } else if (boardFull) {
            return new Game(id, createdAt, newField, GameStatus.DRAW, player1Id, player2Id,
                    null, null, player1Symbol, player2Symbol, isVsComputer);
        } else {
            return new Game(id, createdAt, newField, GameStatus.IN_PROGRESS, player1Id, player2Id,
                    nextPlayerId, null, player1Symbol, player2Symbol, isVsComputer);
        }
    }

    public UUID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public GameField getCopyField() { return field.copy(); }
    public GameStatus getStatus() { return status; }
    public UUID getPlayer1Id() { return player1Id; }
    public UUID getPlayer2Id() { return player2Id; }
    public UUID getCurrentPlayerId() { return currentPlayerId; }
    public UUID getWinnerId() { return winnerId; }
    public int getPlayer1Symbol() { return player1Symbol; }
    public int getPlayer2Symbol() { return player2Symbol; }
    public boolean isVsComputer() { return isVsComputer; }

    public boolean isGameOver() {
        return status == GameStatus.FINISHED || status == GameStatus.DRAW;
    }

    public int getWinner() {
        if (winnerId == null) return 0;
        return winnerId.equals(player1Id) ? player1Symbol : player2Symbol;
    }
}
