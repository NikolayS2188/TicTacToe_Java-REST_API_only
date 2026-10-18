package com.karkaror.tictactoe.domain.service;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.LeaderboardEntry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Расширенный интерфейс сервиса игры, включающий
 * операции доступа к данным для многопользовательской игры.
 * Не наследует GameProcessor, чтобы избежать циклической зависимости.
 */
public interface GameService {

    /**
     * Находит игру по идентификатору.
     * @param gameId идентификатор
     * @return Optional с игрой, если найдена
     */
    Optional<Game> findById(UUID gameId);

    /**
     * Создает новую игру.
     * @param playerId UUID создателя игры
     * @param opponentId UUID оппонента (null для игры с компьютером)
     * @param vsComputer true если игра против компьютера
     * @return созданная игра
     */
    Game createGame(UUID playerId, UUID opponentId, boolean vsComputer);

    /**
     * Присоединяет игрока к существующей игре.
     * @param gameId идентификатор игры
     * @param playerId UUID присоединяющегося игрока
     * @return обновленная игра
     * @throws RuntimeException если игра не найдена или уже имеет двух игроков
     */
    Game joinGame(UUID gameId, UUID playerId);

    /**
     * Выполняет ход в игре.
     * @param gameId идентификатор игры
     * @param row строка (0-2)
     * @param col столбец (0-2)
     * @param playerId UUID игрока, делающего ход
     * @return обновленная игра
     * @throws RuntimeException если ход невозможен
     */
    Game makeMove(UUID gameId, int row, int col, UUID playerId);

    /**
     * Находит все доступные игры (ожидающие второго игрока).
     * @return список доступных игр
     */
    List<Game> findAvailableGames();

    /**
     * Проверяет, является ли пользователь участником игры.
     * @param gameId идентификатор игры
     * @param playerId UUID пользователя
     * @return true если пользователь является участником
     */
    boolean isPlayerInGame(UUID gameId, UUID playerId);

    /**
     * Возвращает все завершённые игры пользователя.
     * Завершённые — это игры со статусом FINISHED (победа) или DRAW (ничья),
     * где пользователь является первым или вторым игроком.
     * Игры отсортированы от самой новой.
     * @param userId UUID пользователя
     * @return список завершённых игр пользователя (сначала новые)
     */
    List<Game> findFinishedGamesByUserId(UUID userId);

    /**
     * Возвращает N лучших игроков по соотношению побед к поражениям.
     * Список отсортирован по убыванию winRatio (победы / (победы + поражения)).
     * Игроки без завершённых игр имеют winRatio = 0.
     * @param limit максимальное количество игроков в результате
     * @return список LeaderboardEntry — таблица лидеров, отсортированная по убыванию winRatio
     */
    List<LeaderboardEntry> getTopPlayers(int limit);
}
