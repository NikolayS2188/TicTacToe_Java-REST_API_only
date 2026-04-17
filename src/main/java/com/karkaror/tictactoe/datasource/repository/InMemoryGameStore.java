package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.datasource.model.GameEntity;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

/**
 * Хранилище игр в оперативной памяти с потокобезопасным доступом с помощью ConcurrentHashMap.
 */
public class InMemoryGameStore {
    // ConcurrentHashMap обеспечивает безопасность при параллельном доступе без необходимости внешней синхронизации
    private final ConcurrentHashMap<UUID, GameEntity> storage = new ConcurrentHashMap<>();

    public void save(UUID id, GameEntity entity) {
        storage.put(id, entity);
    }

    public GameEntity findById(UUID id) {
        return storage.get(id);
    }

    public void delete(UUID id) {
        storage.remove(id);
    }
}