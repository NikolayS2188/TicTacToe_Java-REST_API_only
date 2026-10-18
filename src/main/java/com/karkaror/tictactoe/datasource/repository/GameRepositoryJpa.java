package com.karkaror.tictactoe.datasource.repository;

import com.karkaror.tictactoe.datasource.model.GameEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * JPA репозиторий для работы с играми в базе данных.
 * Расширяет CrudRepository, предоставляя базовые CRUD операции.
 */
@Repository
public interface GameRepositoryJpa extends CrudRepository<GameEntity, UUID> {
    /**
     * Находит игры в статусе WAITING_FOR_PLAYERS (ожидание второго игрока).
     * @param status статус WAITING_FOR_PLAYERS
     * @return список доступных игр
     */
    List<GameEntity> findByStatusAndPlayer2IdIsNull(String status);

    /**
     * Находит завершённые игры пользователя.
     * Завершённая = FINISHED или DRAW.
     */
    @Query("SELECT g FROM GameEntity g " +
            "WHERE (g.player1Id = :userId OR g.player2Id = :userId) " +
            "AND g.status IN ('FINISHED', 'DRAW') " +
            "ORDER BY g.createdAt DESC")
    List<GameEntity> findFinishedGamesByUserId(@Param("userId") UUID userId);

    /**
     * Находит топ-N игроков по соотношению побед.
     * Возвращает проекцию с базовыми полями и вычисленным winRatio.
     */
    @Query(value = """
        WITH stats AS (
            SELECT
                u.id AS userId,
                u.login AS login,
                SUM(CASE WHEN g.winner_id = u.id THEN 1 ELSE 0 END) AS wins,
                SUM(CASE WHEN g.winner_id IS NOT NULL AND g.winner_id != u.id THEN 1 ELSE 0 END) AS losses,
                SUM(CASE WHEN g.status = 'DRAW' THEN 1 ELSE 0 END) AS draws
            FROM users u
            INNER JOIN games g ON (g.player1_id = u.id OR g.player2_id = u.id)
                AND g.status IN ('FINISHED', 'DRAW')
            GROUP BY u.id, u.login
        )
        SELECT
            userId,
            login,
            wins,
            losses,
            draws,
            CASE
                WHEN (wins + losses) = 0 THEN 0
                ELSE CAST(wins AS DOUBLE PRECISION) / (wins + losses)
            END AS winRatio
        FROM stats
        ORDER BY winRatio DESC, wins DESC, login
        LIMIT :limit
        """, nativeQuery = true)
    List<LeaderboardProjection> findTopPlayers(@Param("limit") int limit);
}
