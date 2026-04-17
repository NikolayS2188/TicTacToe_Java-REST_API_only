package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.datasource.model.GameEntity;
import com.karkaror.tictactoe.datasource.mapper.GameDataMapper;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Реализация репозитория, использующая InMemoryGameStore и маппер.
 */
public class GameRepositoryImpl implements GameRepository {
    private final InMemoryGameStore store;
    private final GameDataMapper mapper;

    public GameRepositoryImpl(InMemoryGameStore store, GameDataMapper mapper) {
        this.store = store;
        this.mapper = mapper;
    }

    @Override
    public Game save(Game game) {
        GameEntity entity = mapper.toEntity(game);
        store.save(game.getId(), entity);
        return game; // возвращаем тот же объект для удобства
    }

    @Override
    public Optional<Game> findById(UUID id) {
        GameEntity entity = store.findById(id);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity));
    }
}