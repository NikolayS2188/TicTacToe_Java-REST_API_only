package com.karkaror.tictactoe.datasource.repository;

import java.util.UUID;

/**
 * Проекция для таблицы лидеров.
 * Spring Data JPA автоматически создаёт реализацию этого интерфейса
 * и маппит на неё результат запроса.
 * Ключевое: имена геттеров должны совпадать с алиасами в SQL (AS userId, AS wins, ...).
 */
public interface LeaderboardProjection {

    /** UUID пользователя. */
    UUID getUserId();

    /** Логин пользователя. */
    String getLogin();

    /** Количество побед. */
    long getWins();

    /** Количество поражений. */
    long getLosses();

    /** Количество ничьих. */
    long getDraws();

    /** Соотношение побед: wins / (wins + losses). 0, если нет завершённых игр. */
    double getWinRatio();
}
