package com.karkaror.tictactoe.datasource.service;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.service.GameProcessor;
import com.karkaror.tictactoe.domain.service.GameService;
import com.karkaror.tictactoe.datasource.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

/**
 * Адаптер, реализующий интерфейс GameService.
 * Сочетает бизнес-логику (через GameProcessor) и работу с хранилищем (через GameRepository).
 */

public class GameServiceAdapter implements GameService {
    private final GameRepository repository;
    private final GameProcessor gameProcessor; // чистая логика, например MinimaxGameProcessor

    public GameServiceAdapter(GameRepository repository, GameProcessor gameProcessor) {
        this.repository = repository;
        this.gameProcessor = gameProcessor;
    }

    @Override
    public Game makeComputerMove(Game game) {
        // Вычисляем ход компьютера с помощью процессора
        Game updatedGame = gameProcessor.makeComputerMove(game);
        // Сохраняем обновленное состояние в хранилище
        return repository.save(updatedGame);
    }

    @Override
    public boolean isValidMove(Game game, int row, int col, int playerId) {
        return gameProcessor.isValidMove(game, row, col, playerId);
    }

    @Override
    public boolean isGameOver(Game game) {
        return gameProcessor.isGameOver(game);
    }

    @Override
    public Optional<Game> findById(UUID gameId) {
        return repository.findById(gameId);
    }


    @Override
    public Game save(Game game) {
        return repository.save(game);
    }
}