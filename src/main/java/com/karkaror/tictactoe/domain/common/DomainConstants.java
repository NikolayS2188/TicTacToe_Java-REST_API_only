package com.karkaror.tictactoe.domain.common;

import java.util.UUID;

public class DomainConstants {
    /** Размер квадратной матрицы. */
    public static final int SQUARE_SIZE = 3;

    /** Количество строк игрового поля. */
    public static final int ROWS = SQUARE_SIZE;

    /** Количество столбцов игрового поля. */
    public static final int COLUMNS = SQUARE_SIZE;

    /** Суммарное количество позиций на поле. */
    public static final int FIELD_SIZE = ROWS * COLUMNS;

    /** Занятая позиция игрового поля первого игрока - Минимизатор в контексте Минимакса. */
    public static final int PLAYER1 = 1;

    /** Занятая позиция игрового поля второго игрока - Максимизатор в контексте Минимакса. */
    public static final int PLAYER2 = 2;

    /** Незаполненная (свободная) игровая позиция поля. */
    public static final int EMPTY = 0;

    /** Начальная глубина рекурсии. */
    public static final int INITIAL_RECURSION_DEPTH = 0;

    /** Флаг вызова минимизатора для функции minimax(). */
    public static final boolean MINIMIZER_CALL_FLAG = false;

    /** Флаг вызова максимизатора для функции minimax(). */
    public static final boolean MAXIMIZER_CALL_FLAG = true;

    /** Игра ещё не завершена. */
    public static final int GAME_CONTINUES = 0;

    /** Лучшее максимальное оценочное значение. По модулю - т.к.:
     *       победа Минимизатора должна быть всегда < 0 (поэтому для расчета нужно брать со знаком минус);
     *       победа Максимизатора должны быть всегда > 0 (в расчете брать со знаком плюс).
     * */
    public static final int MAX_SCORE_VALUES = FIELD_SIZE + 1;

    /** Оценочное значение для ничьи. Одинаково (симметрично) для Минимизатора и Максимизатора. */
    public static final int DRAW_SCORE_VALUES = 0;

    /**
     * UUID компьютера (системный игрок).
     * Используется max UUID, чтобы избежать конфликта с "отсутствием" nil UUID (00000000-0000-0000-0000-000000000000).
     */
    public static final UUID COMPUTER_UUID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");


    private DomainConstants() {}
}
