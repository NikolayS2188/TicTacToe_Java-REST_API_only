package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.domain.model.Game;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс репозитория для работы с играми.
 */
public interface GameRepository {
    Game save(Game game);
    Optional<Game> findById(UUID id);
}