package com.karkaror.tictactoe.web.controller;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.model.LeaderboardEntry;
import com.karkaror.tictactoe.domain.service.GameService;
import com.karkaror.tictactoe.web.mapper.GameWebMapper;
import com.karkaror.tictactoe.web.model.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST-контроллер для игры в крестики-нолики.
 * Обрабатывает запросы на создание игры, выполнение ходов,
 * подключение к игре и получение информации об играх.
 */
@RestController
@RequestMapping("/game")
@Validated
public class GameController {

    private final GameService gameService;
    private final GameWebMapper webMapper;

    public GameController(GameService gameService, GameWebMapper webMapper) {
        this.gameService = gameService;
        this.webMapper = webMapper;
    }

    /**
     * Создает новую игру.
     * @param vsComputer true если игра против компьютера
     * @param userId UUID текущего пользователя (из SecurityContext)
     * @return состояние созданной игры (201 Created)
     *         или ошибку (400 Bad Request — некорректные параметры,
     *                     401 Unauthorized — аутентификация отсутствует)
     */
    @PostMapping("/create")
    public ResponseEntity<GameStateResponse> createGame(
            @RequestParam(defaultValue = "false") boolean vsComputer,
            @AuthenticationPrincipal UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GameStateResponse(null, null, "UNAUTHORIZED", null, null, "Not authenticated"));
        }

        try {
            Game game = gameService.createGame(userId, null, vsComputer);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(webMapper.toResponse(game, "Game created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GameStateResponse(null, null, "ERROR", null, null, e.getMessage()));
        }
    }

    /**
     * Получает список доступных игр (ожидающих второго игрока).
     * @return список игр
     */
    @GetMapping("/available")
    public ResponseEntity<List<GameInfoResponse>> getAvailableGames() {
        List<Game> games = gameService.findAvailableGames();
        List<GameInfoResponse> response = games.stream()
                .map(game -> new GameInfoResponse(
                        game.getId(),
                        game.getPlayer1Id(),
                        game.getPlayer2Id(),
                        game.getStatus().name(),
                        game.isVsComputer()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Присоединяет пользователя к существующей игре.
     * @param gameId идентификатор игры
     * @param userId UUID текущего пользователя (из SecurityContext)
     * @return состояние игры после подключения (200 OK)
     *         или ошибку (400 Bad Request — игра не найдена или уже полна,
     *                     401 Unauthorized — аутентификация отсутствует)
     */
    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameStateResponse> joinGame(
            @PathVariable UUID gameId,
            @AuthenticationPrincipal UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GameStateResponse(null, null, "UNAUTHORIZED", null, null, "Not authenticated"));
        }

        try {
            Game game = gameService.joinGame(gameId, userId);
            return ResponseEntity.ok(webMapper.toResponse(game, "Successfully joined the game"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GameStateResponse(gameId, null, "ERROR", null, null, e.getMessage()));
        }
    }

    /**
     * Получает текущее состояние игры.
     * @param gameId идентификатор игры
     * @param userId UUID текущего пользователя (из SecurityContext)
     * @return состояние игры (200 OK)
     *         или ошибку (401 Unauthorized — аутентификация отсутствует,
     *                     403 Forbidden — пользователь не участник игры,
     *                     404 Not Found — игра не найдена)
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateResponse> getGame(
            @PathVariable UUID gameId,
            @AuthenticationPrincipal UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GameStateResponse(null, null, "UNAUTHORIZED", null, null, "Not authenticated"));
        }

        try {
            if (!gameService.isPlayerInGame(gameId, userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new GameStateResponse(gameId, null, "FORBIDDEN", null, null, "You are not in this game"));
            }

            Game game = gameService.findById(gameId)
                    .orElseThrow(() -> new RuntimeException("Game not found"));
            return ResponseEntity.ok(webMapper.toResponse(game));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GameStateResponse(gameId, null, "ERROR", null, null, e.getMessage()));
        }
    }

    /**
     * Выполняет ход в игре.
     * @param gameId идентификатор игры
     * @param request координаты хода
     * @param userId UUID текущего пользователя (из SecurityContext)
     * @return обновленное состояние игры (200 OK)
     *         или ошибку (400 Bad Request — некорректный ход,
     *                     401 Unauthorized — аутентификация отсутствует)
     */
    @PostMapping("/{gameId}/move")
    public ResponseEntity<GameStateResponse> makeMove(
            @PathVariable UUID gameId,
            @Valid @RequestBody GameMoveRequest request,
            @AuthenticationPrincipal UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new GameStateResponse(null, null, "UNAUTHORIZED", null, null, "Not authenticated"));
        }

        try {
            Game game = gameService.makeMove(gameId, request.row(), request.col(), userId);
            return ResponseEntity.ok(webMapper.toResponse(game));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new GameStateResponse(gameId, null, "ERROR", null, null, e.getMessage()));
        }
    }

    /**
     * Получить все завершённые игры текущего пользователя.
     * Endpoint доступен только авторизованным пользователям.
     * Игры отсортированы от самой новой.
     * @param userId UUID текущего пользователя (из SecurityContext)
     * @return ResponseEntity со списком GameInfoResponse завершённых игр (200 OK)
     *         или ошибку (401 Unauthorized — аутентификация отсутствует)
     */
    @GetMapping("/history")
    public ResponseEntity<List<GameInfoResponse>> getHistory(
            @AuthenticationPrincipal UUID userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Game> games = gameService.findFinishedGamesByUserId(userId);
        List<GameInfoResponse> response = games.stream()
                .map(game -> new GameInfoResponse(
                        game.getId(),
                        game.getPlayer1Id(),
                        game.getPlayer2Id(),
                        game.getStatus().name(),
                        game.isVsComputer()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * Возвращает таблицу лидеров.
     * @param limit количество игроков (1-100, по умолчанию 10)
     * @return список лучших игроков с соотношением побед (200 OK)
     *         или ошибка (400 Bad Request — некорректный параметр limit)
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "limit must be at least 1")
            @Max(value = 100, message = "limit must be at most 100")
            int limit) {
        return ResponseEntity.ok(gameService.getTopPlayers(limit));
    }
}
