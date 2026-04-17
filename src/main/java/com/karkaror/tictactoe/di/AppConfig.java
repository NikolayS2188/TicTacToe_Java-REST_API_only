package com.karkaror.tictactoe.di;

import com.karkaror.tictactoe.datasource.mapper.GameDataMapper;
import com.karkaror.tictactoe.datasource.repository.GameRepository;
import com.karkaror.tictactoe.datasource.repository.GameRepositoryImpl;
import com.karkaror.tictactoe.datasource.repository.InMemoryGameStore;
import com.karkaror.tictactoe.datasource.service.GameServiceAdapter;
import com.karkaror.tictactoe.domain.service.GameProcessor;
import com.karkaror.tictactoe.domain.service.GameService;
import com.karkaror.tictactoe.domain.service.MinimaxGameProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс Spring, объявляющий бины вручную.
 * Все бины имеют область видимости singleton (по умолчанию).
 */
@Configuration
public class AppConfig {

    @Bean
    public InMemoryGameStore inMemoryGameStore() {
        // Потокобезопасное хранилище игр
        return new InMemoryGameStore();
    }

    @Bean
    public GameDataMapper gameDataMapper() {
        // Маппер между сущностью и доменной моделью
        return new GameDataMapper();
    }

    @Bean
    public GameRepository gameRepository(InMemoryGameStore store, GameDataMapper mapper) {
        // Репозиторий для работы с играми
        return new GameRepositoryImpl(store, mapper);
    }

    @Bean
    public GameProcessor gameProcessor() {
        // Реализация логики игры (алгоритм Минимакс)
        return new MinimaxGameProcessor();
    }

    @Bean
    public GameService gameService(GameRepository repository, GameProcessor processor) {
        // Адаптер, объединяющий доступ к данным и бизнес-логику
        return new GameServiceAdapter(repository, processor);
    }
}