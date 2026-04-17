# s21_Тic-Tac-Toe_Game_REST_API

## Описание

REST API для игры Крестики-Нолики.
<br/>Взаимодействие через HTTP запросы:

#### Создание игры:

| ОС                          | Команда                                                                         |
|-----------------------------|---------------------------------------------------------------------------------|
| Mac / Linux / Windows (cmd) | curl -X POST http://localhost:8080/game -H "Content-Type: application/json"     |
| Windows (PowerShell)        | curl.exe -X POST http://localhost:8080/game -H "Content-Type: application/json" |

#### Сделать ход (замените GAME-ID на реальный):

| ОС                          | Команда                                                                                                                |
|-----------------------------|------------------------------------------------------------------------------------------------------------------------|
| Mac / Linux / Windows (cmd) | curl -X POST http://localhost:8080/game/GAME-ID -H "Content-Type: application/json" -d "{\\"row\\":0,\\"col\\":0}"     |
| Windows (PowerShell)        | curl.exe -X POST http://localhost:8080/game/GAME-ID -H "Content-Type: application/json" -d '{\\"row\\":0,\\"col\\":0}' |