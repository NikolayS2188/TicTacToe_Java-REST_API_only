package com.karkaror.tictactoe.domain.service;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.GameField;
import com.karkaror.tictactoe.domain.util.GameRules;

import static com.karkaror.tictactoe.domain.common.DomainConstants.*;

/**
 * Реализация GameProcessor на основе алгоритма Минимакс с альфа-бета отсечением.
 * Вычисляет оптимальный ход для компьютера (игрок 2).
 */
public class MinimaxGameProcessor implements GameProcessor {

    @Override
    public Game makeComputerMove(Game game) {
        if (game.isGameOver()) {
            return game; // игра уже окончена, ход невозможен
        }

        GameField field = game.getCopyField();
        Integer[][] matrix = field.getMatrix();
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        // Перебираем все пустые клетки
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (matrix[i][j] == EMPTY) {
                    // Пробуем ход компьютера
                    Integer[][] newMatrix = copyMatrix(matrix);
                    newMatrix[i][j] = PLAYER2; // компьютер
                    int score = minimax(newMatrix, INITIAL_RECURSION_DEPTH, MINIMIZER_CALL_FLAG, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        if (bestRow == -1 || bestCol == -1) {
            // нет свободных клеток (ничья)
            return game;
        }

        return game.withMove(bestRow, bestCol, PLAYER2);
    }

    @Override
    public boolean isValidMove(Game game, int row, int col, int playerId) {
        // Проверяем, что игра не окончена, координаты в пределах, клетка пуста
        if (game.isGameOver()) return false;
        if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) return false;
        GameField field = game.getCopyField();
        Integer[][] matrix = field.getMatrix();
        return matrix[row][col] == EMPTY;
    }

    @Override
    public boolean isGameOver(Game game) {
        return game.isGameOver();
    }

    /**
     * Реализация алгоритма Минимакс с альфа-бета отсечением.
     * @param matrix текущая матрица игрового поля
     * @param depth глубина рекурсии
     * @param isMaximizing true - ход компьютера (максимизация), false - ход игрока (минимизация)
     * @param alpha текущее альфа (лучший результат для максимизирующего)
     * @param beta текущее бета (лучший результат для минимизирующего)
     * @return оценка состояния с точки зрения компьютера
     */
    private int minimax(Integer[][] matrix, int depth, boolean isMaximizing, int alpha, int beta) {
        int winner = GameRules.check3x3BoardWinner(matrix);
        if (winner == PLAYER2) return MAX_SCORE_VALUES - depth; // компьютер выиграл, предпочитаем более быструю победу
        if (winner == PLAYER1) return -MAX_SCORE_VALUES + depth; // игрок выиграл, предпочитаем более медленное поражение
        if (GameRules.isBoardFull(matrix)) return DRAW_SCORE_VALUES; // ничья

        if (isMaximizing) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    if (matrix[i][j] == EMPTY) {
                        matrix[i][j] = PLAYER2; // компьютер
                        int score = minimax(matrix, depth + 1, MINIMIZER_CALL_FLAG, alpha, beta);
                        matrix[i][j] = EMPTY; // откат
                        best = Math.max(score, best);
                        alpha = Math.max(alpha, best);
                        if (beta <= alpha) break; // отсечение
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < ROWS; i++) {
                for (int j = 0; j < COLUMNS; j++) {
                    if (matrix[i][j] == EMPTY) {
                        matrix[i][j] = PLAYER1; // игрок
                        int score = minimax(matrix, depth + 1, MAXIMIZER_CALL_FLAG, alpha, beta);
                        matrix[i][j] = EMPTY; // откат
                        best = Math.min(score, best);
                        beta = Math.min(beta, best);
                        if (beta <= alpha) break; // отсечение
                    }
                }
            }
            return best;
        }
    }

    private Integer[][] copyMatrix(Integer[][] matrix) {
        Integer[][] copy = new Integer[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(matrix[i], 0, copy[i], 0, COLUMNS);
        }
        return copy;
    }
}
