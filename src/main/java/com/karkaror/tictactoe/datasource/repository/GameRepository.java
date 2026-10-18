package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.LeaderboardEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс репозитория для работы с играми.
 */
public interface GameRepository {
    Game save(Game game);
    Optional<Game> findById(UUID id);
    List<Game> findAvailableGames();
    boolean isPlayerInGame(UUID gameId, UUID playerId);
    List<Game> findFinishedGamesByUserId(UUID userId);
    List<LeaderboardEntry> findTopPlayers(int limit);
}
