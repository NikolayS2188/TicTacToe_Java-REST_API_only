package com.karkaror.tictactoe.web.controller;

import com.karkaror.tictactoe.domain.model.Game;
import com.karkaror.tictactoe.domain.service.GameService;
import com.karkaror.tictactoe.web.model.GameMoveRequest;
import com.karkaror.tictactoe.web.model.GameStateResponse;
import com.karkaror.tictactoe.web.mapper.GameWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * REST-контроллер для игры в крестики-нолики.
 * Обрабатывает запросы на выполнение хода.
 */
@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;    // Сервис игры (адаптер)
    private final GameWebMapper webMapper;    // Маппер доменной модели в DTO

    public GameController(GameService gameService, GameWebMapper webMapper) {
        this.gameService = gameService;
        this.webMapper = webMapper;
    }

    /**
     * Создает новую игру с пустым полем.
     * @return состояние новой игры
     */
    @PostMapping
    public ResponseEntity<GameStateResponse> createGame() {
        // Создаем новую игру со случайным UUID
        Game newGame = new Game(UUID.randomUUID());
        // Сохраняем в хранилище
        Game savedGame = gameService.save(newGame);
        // Возвращаем состояние
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(webMapper.toResponse(savedGame));
    }

    /**
     * Обрабатывает ход игрока и возвращает обновлённое состояние после хода компьютера.
     * @param gameId  идентификатор игры (UUID)
     * @param request тело запроса с координатами хода игрока (row, col)
     * @return ResponseEntity с состоянием игры и HTTP-статусом 200
     * @throws GameNotFoundException если игра с указанным ID не найдена
     * @throws InvalidMoveException  если ход игрока недопустим
     */
    @PostMapping("/{gameId}")
    public ResponseEntity<GameStateResponse> makeMove(
            @PathVariable UUID gameId,
            @RequestBody GameMoveRequest request) {

        // Загружаем игру из хранилища через сервис
        Game game = gameService.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found with id: " + gameId));

        // Проверяем валидность хода игрока (клетка пуста, игра не окончена)
        if (!gameService.isValidMove(game, request.row(), request.col(), 1)) {
            throw new InvalidMoveException("Invalid move at (" + request.row() + "," + request.col() + ")");
        }

        // Выполняем ход игрока (создаём новое состояние игры)
        Game afterPlayerMove = game.withMove(request.row(), request.col(), 1);

        // Проверяем, не закончилась ли игра после хода игрока
        if (afterPlayerMove.isGameOver()) {
            // Если игрок выиграл или ничья, сохраняем и возвращаем
            Game saved = gameService.makeComputerMove(afterPlayerMove);
            return ResponseEntity.ok(webMapper.toResponse(saved));
        }

        // Иначе выполняем ход компьютера (сервис сам сохранит новое состояние) и возвращаем обновлённое состояние
        Game afterComputerMove = gameService.makeComputerMove(afterPlayerMove);
        return ResponseEntity.ok(webMapper.toResponse(afterComputerMove));
    }

    /**
     * Исключение игра не найдена, выбрасываемое при отсутствии игры.
     * Аннотация @ResponseStatus преобразует его в HTTP 404 Not Found.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class GameNotFoundException extends RuntimeException {
        public GameNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Исключение недопустимый ход, выбрасываемое при недопустимом ходе.
     * Аннотация @ResponseStatus преобразует его в HTTP 400 Bad Request.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidMoveException extends RuntimeException {
        public InvalidMoveException(String message) {
            super(message);
        }
    }
}
