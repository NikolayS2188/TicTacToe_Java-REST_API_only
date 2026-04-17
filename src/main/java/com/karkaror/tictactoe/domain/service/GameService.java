package com.karkaror.tictactoe.domain.service;

import com.karkaror.tictactoe.domain.model.Game;
import java.util.Optional;
import java.util.UUID;

/**
 * Расширенный интерфейс сервиса игры, включающий операции доступа к данным.
 * Объединяет бизнес-логику (GameProcessor) и работу с хранилищем.
 */
public interface GameService extends GameProcessor {


    /**
     * Находит игру по идентификатору.
     * @param gameId идентификатор
     * @return Optional с игрой, если найдена
     */
    Optional<Game> findById(UUID gameId);

    /**
     * Сохраняет новую игру в хранилище.
     * @param game игра для сохранения
     * @return сохраненная игра
     */
    Game save(Game game);
}