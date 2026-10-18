package com.karkaror.tictactoe.datasource.service;

import com.karkaror.tictactoe.datasource.repository.GameRepository;
import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.LeaderboardEntry;
import com.karkaror.tictactoe.domain.service.GameProcessor;
import com.karkaror.tictactoe.domain.service.GameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.karkaror.tictactoe.domain.common.DomainConstants.COMPUTER_UUID;

/**
 * Реализация сервиса игры.
 * Объединяет бизнес-логику (через GameProcessor) и работу с хранилищем (через GameRepository).
 */
@Service
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository repository;
    private final GameProcessor gameProcessor;

    public GameServiceImpl(GameRepository repository,
                           GameProcessor gameProcessor) {
        this.repository = repository;
        this.gameProcessor = gameProcessor;
    }

    public Game makeComputerMove(Game game) {
        Game updatedGame = gameProcessor.makeComputerMove(game);
        return repository.save(updatedGame);
    }

    public boolean isGameOver(Game game) {
        return gameProcessor.isGameOver(game);
    }

    @Override
    public Optional<Game> findById(UUID gameId) {
        return repository.findById(gameId);
    }

    @Override
    public Game createGame(UUID playerId, UUID opponentId, boolean vsComputer) {
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, playerId, vsComputer);

        if (vsComputer) {
            game = game.withPlayerJoined(COMPUTER_UUID);
        }

        return repository.save(game);
    }

    @Override
    public Game joinGame(UUID gameId, UUID playerId) {
        Game game = findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (game.getPlayer1Id().equals(playerId)) {
            throw new RuntimeException("You are already in this game");
        }

        if (game.getPlayer2Id() != null) {
            throw new RuntimeException("Game already has two players");
        }

        Game updatedGame = game.withPlayerJoined(playerId);
        return repository.save(updatedGame);
    }

    @Override
    public Game makeMove(UUID gameId, int row, int col, UUID playerId) {
        Game game = findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        if (!isPlayerInGame(gameId, playerId)) {
            throw new RuntimeException("You are not a player in this game");
        }

        if (game.isVsComputer() && playerId.equals(game.getPlayer2Id())) {
            throw new RuntimeException("Computer is the opponent");
        }

        if (!game.getCurrentPlayerId().equals(playerId)) {
            throw new RuntimeException("Not your turn");
        }

        Game afterPlayerMove = game.withMove(row, col, playerId);

        if (afterPlayerMove.isGameOver()) {
            return repository.save(afterPlayerMove);
        }

        if (afterPlayerMove.isVsComputer() &&
                afterPlayerMove.getCurrentPlayerId().equals(afterPlayerMove.getPlayer2Id())) {
            Game afterComputerMove = gameProcessor.makeComputerMove(afterPlayerMove);
            return repository.save(afterComputerMove);
        }

        return repository.save(afterPlayerMove);
    }

    @Override
    public List<Game> findAvailableGames() {
        return repository.findAvailableGames();
    }

    @Override
    public boolean isPlayerInGame(UUID gameId, UUID playerId) {
        return repository.isPlayerInGame(gameId, playerId);
    }

    @Override
    public List<Game> findFinishedGamesByUserId(UUID userId) {
        return repository.findFinishedGamesByUserId(userId);
    }

    @Override
    public List<LeaderboardEntry> getTopPlayers(int limit) {
        return repository.findTopPlayers(limit);
    }
}
