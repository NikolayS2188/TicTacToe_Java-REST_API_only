package com.karkaror.tictactoe.domain.common;

public class DomainConstants {
    /** Размер квадратной матрицы. */
    static public final int SQUARE_SIZE = 3;
    /** Количество строк игрового поля. */
    static public final int ROWS = SQUARE_SIZE;
    /** Количество столбцов игрового поля. */
    static public final int COLUMNS = SQUARE_SIZE;
    /** Суммарное количество позиций на поле. */
    static public final int FIELD_SIZE = ROWS * COLUMNS;
    /** Идентификатор или занятая позиция игрового поля первого игрока - Минимизатор в контексте Минимакса. */
    static public final int PLAYER1 = 1;
    /** Идентификатор или занятая позиция игрового поля второго игрока - Максимизатор в контексте Минимакса. */
    static public final int PLAYER2 = 2;
    /** Незаполненная (свободная) игровая позиция поля. */
    static public final int EMPTY = 0;
    /** Начальная глубина рекурсии. */
    static public final int INITIAL_RECURSION_DEPTH = 0;
    /** Флаг вызова минимизатора для функции minimax(). */
    static public final boolean MINIMIZER_CALL_FLAG = false;
    /** Флаг вызова максимизатора для функции minimax(). */
    static public final boolean MAXIMIZER_CALL_FLAG = true;
    /** Игра ещё не завершена. */
    static public final int GAME_CONTINUES = 0;

    /** Лучшее максимальное оценочное значение. По модулю - т.к.:
     *       победа Минимизатора должна быть всегда < 0 (поэтому для расчета нужно брать со знаком минус);
     *       победа Максимизатора должны быть всегда > 0 (в расчете брать со знаком плюс).
     * */
    static public final int MAX_SCORE_VALUES = ROWS * COLUMNS + 1;
    /** Оценочное значение для ничьи. Одинаково (симметрично) для Минимизатора и Максимизатора. */
    static public final int DRAW_SCORE_VALUES = 0;



    private DomainConstants() {}
}
