package com.karkaror.tictactoe.domain.model;

import java.util.UUID;

/**
 * Запись в таблице лидеров.
 */
public record LeaderboardEntry(
        UUID userId,
        String login,
        long wins,
        long losses,
        long draws,
        double winRatio
) {}