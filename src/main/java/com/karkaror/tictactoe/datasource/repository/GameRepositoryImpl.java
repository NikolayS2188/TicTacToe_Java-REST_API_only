package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.datasource.mapper.GameDataMapper;
import com.karkaror.tictactoe.datasource.model.GameEntity;
import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.LeaderboardEntry;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация репозитория для работы с играми через JPA.
 * Использует GameRepositoryJpa для доступа к БД и GameDataMapper для преобразования.
 */
@Repository
public class GameRepositoryImpl implements GameRepository {

    private final GameRepositoryJpa jpaRepository;
    private final GameDataMapper mapper;

    public GameRepositoryImpl(GameRepositoryJpa jpaRepository, GameDataMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Game save(Game game) {
        GameEntity entity = mapper.toEntity(game);
        GameEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Game> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    /**
     * Находит все доступные игры (ожидающие второго игрока).
     * @return список доступных игр
     */
    @Override
    public List<Game> findAvailableGames() {
        List<GameEntity> entities = jpaRepository.findByStatusAndPlayer2IdIsNull("WAITING_FOR_PLAYERS");
        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, является ли пользователь участником игры.
     * @param gameId идентификатор игры
     * @param userId идентификатор пользователя
     * @return true если пользователь является участником
     */
    @Override
    public boolean isPlayerInGame(UUID gameId, UUID userId) {
        return jpaRepository.findById(gameId)
                .map(entity -> entity.getPlayer1Id().equals(userId) ||
                             (entity.getPlayer2Id() != null && entity.getPlayer2Id().equals(userId)))
                .orElse(false);
    }

    @Override
    public List<Game> findFinishedGamesByUserId(UUID userId) {
        return jpaRepository.findFinishedGamesByUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaderboardEntry> findTopPlayers(int limit) {
        return jpaRepository.findTopPlayers(limit).stream()
                .map(this::toLeaderboardEntry)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует проекцию в доменную модель LeaderboardEntry.
     */
    private LeaderboardEntry toLeaderboardEntry(LeaderboardProjection p) {
        return new LeaderboardEntry(
                p.getUserId(),
                p.getLogin(),
                p.getWins(),
                p.getLosses(),
                p.getDraws(),
                p.getWinRatio()
        );
    }
}
