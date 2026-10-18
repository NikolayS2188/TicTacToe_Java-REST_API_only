# s21_Тic-Tac-Toe_Game_REST_API

## Описание

Backend для игры Крестики-Нолики с поддержкой:
- игры против компьютера (Minimax с альфа-бета отсечением);
- игры между двумя игроками;
- регистрации и авторизации пользователей (JWT);
- хранения данных в PostgreSQL;
- история завершённых игр;
- таблица лидеров.

---

<a id="содержание"></a>
## Содержание

1. [Описание](#описание)
2. [Запуск приложения](#запуск-приложения)
   - [1. Запустить PostgreSQL](#1-запустить-postgresql)
   - [2. Настроить application.properties](#2-настроить-applicationproperties)
   - [3. Запустить приложение](#3-запустить-приложение)
3. [API](#api)
   - [Аутентификация](#аутентификация)
   - [Эндпоинты](#эндпоинты)
4. [Примеры curl-запросов](#примеры-curl-запросов)
   - [1. Аутентификация](#1-аутентификация)
      - [1.1. Регистрация пользователя](#11-регистрация-пользователя)
      - [1.2. Логин](#12-логин)
      - [1.3. Обновить accessToken](#13-обновить-accesstoken)
      - [1.4. Обновить refreshToken](#14-обновить-refreshtoken)
      - [1.5. Получить информацию о текущем пользователе](#15-получить-информацию-о-текущем-пользователе)
      - [1.6. Ошибка регистрации (логин занят)](#16-ошибка-регистрации-логин-занят)
      - [1.7. Ошибка логина (неверный пароль)](#17-ошибка-логина-неверный-пароль)
      - [1.8. Ошибка авторизации (нет токена)](#18-ошибка-авторизации-нет-токена)
   - [2. Игра против компьютера](#2-игра-против-компьютера)
      - [2.1. Создать игру против компьютера](#21-создать-игру-против-компьютера)
      - [2.2. Сделать ход против компьютера](#22-сделать-ход-против-компьютера)
      - [2.3. Получить состояние игры](#23-получить-состояние-игры)
      - [2.4. Ошибка: ход в занятую клетку](#24-ошибка-ход-в-занятую-клетку)
   - [3. Игра между двумя игроками](#3-игра-между-двумя-игроками)
      - [3.1. Зарегистрировать второго игрока](#31-зарегистрировать-второго-игрока)
      - [3.2. Логин player2](#32-логин-player2)
      - [3.3. Создать игру (player1)](#33-создать-игру-player1)
      - [3.4. Получить список доступных игр (player2)](#34-получить-список-доступных-игр-player2)
      - [3.5. Присоединиться к игре (player2)](#35-присоединиться-к-игре-player2)
      - [3.6. Ход player1](#36-ход-player1)
      - [3.7. Ход player2](#37-ход-player2)
      - [3.8. Ошибка: не ваш ход](#38-ошибка-не-ваш-ход)
      - [3.9. Ошибка: присоединиться к полной игре](#39-ошибка-присоединиться-к-полной-игре)
      - [3.10. Ошибка: игрок не в игре](#310-ошибка-игрок-не-в-игре)
   - [4. Завершение игры](#4-завершение-игры)
      - [4.1. Победа игрока](#41-победа-игрока)
      - [4.2. Ничья](#42-ничья)
   - [5. История игр и таблица лидеров](#5-история-игр-и-таблица-лидеров)
      - [5.1. Получить историю завершённых игр](#51-получить-историю-завершённых-игр)
      - [5.2. Получить таблицу лидеров](#52-получить-таблицу-лидеров)
5. [Очистка базы данных](#очистка-базы-данных)
   - [Просмотр данных](#просмотр-данных)
   - [Удаление конкретного пользователя](#удаление-конкретного-пользователя)
   - [Удаление всех пользователей](#удаление-всех-пользователей)
   - [Удаление всех игр](#удаление-всех-игр)
   - [Полная очистка всех таблиц](#полная-очистка-всех-таблиц)
   - [Полное удаление контейнера и данных](#полное-удаление-контейнера-и-данных)

---

## Запуск приложения

### 1. Запустить PostgreSQL

**Вариант A. В контейнере Docker (в compose.yaml предусмотрено перенаправление локального порта 5433 на 5432 контейнера):**

```bash
docker-compose up -d
```

**Вариант Б. Локально, если tictactoe_db ранее не создана:**

```bash
psql -U postgres
CREATE DATABASE tictactoe_db;
\q
```

### 2. Настроить application.properties

(предусмотрено использование порта 5433)

```properties
server.port=8080
server.servlet.context-path=/api

spring.datasource.url=jdbc:postgresql://localhost:5433/tictactoe_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Configuration
jwt.access-secret=СГЕНЕРИРОВАННЫЙ_BASE64_СЕКРЕТ_1
jwt.refresh-secret=СГЕНЕРИРОВАННЫЙ_BASE64_СЕКРЕТ_2
jwt.access-ttl-minutes=15
jwt.refresh-ttl-days=30
```

**Генерация секретов: 64 случайных байта → Base64 (выполнить дважды):**

Если установлен <code>openssl
```bash
openssl rand -base64 64
```

Для Windows (PowerShell), если не установлен <code>openssl:
```powershell
$bytes = New-Object byte[] 64
[System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($bytes)
[Convert]::ToBase64String($bytes)
```


### 3. Запустить приложение

```bash
./gradlew bootRun
```

Приложение будет доступно по адресу: `http://localhost:8080/api`

[↑ К содержанию](#содержание)

---

## API

### Аутентификация

Все эндпоинты, кроме `/auth/register`, `/auth/login`, `/auth/refresh/access`, `/auth/refresh/refresh`, требуют авторизации.

Заголовок авторизации: `Bearer <accessToken>`

### Эндпоинты

| Метод | Путь | Описание | Авторизация |
|-------|------|----------|-------------|
| POST | `/auth/register` | Регистрация | Не требуется |
| POST | `/auth/login` | Логин | Не требуется |
| POST | `/auth/refresh/access` | Обновить accessToken | Не требуется |
| POST | `/auth/refresh/refresh` | Обновить refreshToken | Не требуется |
| GET | `/auth/user/me` | Информация о текущем пользователе | Требуется |
| POST | `/game/create` | Создать игру | Требуется |
| GET | `/game/available` | Список доступных игр | Требуется |
| POST | `/game/{gameId}/join` | Присоединиться к игре | Требуется |
| GET | `/game/{gameId}` | Получить состояние игры | Требуется |
| POST | `/game/{gameId}/move` | Сделать ход | Требуется |
| GET | `/game/history` | История завершённых игр | Требуется |
| GET | `/game/leaderboard` | Таблица лидеров | Требуется |

[↑ К содержанию](#содержание)

---

## Примеры curl-запросов

> *Примечания:* 
> 
> В примерах используется базовый URL `http://localhost:8080/api`.
> Для корректной передачи JSON:
> - на **Mac/Linux/cmd** используйте `curl` с экранированием кавычек;
> - на **Windows PowerShell** используйте `curl.exe` с экранированием `\"` и флагом `--%` (отключает обработку PowerShell специальных символов $, #, @ и т.д.).
>
> Во всех примерах:
> - `TOKEN` — это `accessToken`, полученный после логина или его обновления.  Подставляется в заголовок Authorization: Bearer <TOKEN>; 
> - `REFRESH_TOKEN` — это `refreshToken`, полученный после логина или его обновления. Подставляется в тело запросов /auth/refresh/access и /auth/refresh/refresh.

---

### 1. Аутентификация

#### 1.1. Регистрация пользователя

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"password123\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"password123\"}"
```

**Ответ (201 Created):**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "login": "player1"
}
```

[↑ К содержанию](#содержание)

---

#### 1.2. Логин

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"password123\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"password123\"}"
```

**Ответ (200 OK):**
```json
{
  "type": "Bearer",
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

[↑ К содержанию](#содержание)

---

#### 1.3. Обновить accessToken

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/refresh/access -H "Content-Type: application/json" -d "{\"refreshToken\":\"REFRESH_TOKEN\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/refresh/access -H "Content-Type: application/json" -d "{\"refreshToken\":\"REFRESH_TOKEN\"}"
```

**Ответ (200 OK):**
```json
{
  "type": "Bearer",
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "REFRESH_TOKEN"
}
```

[↑ К содержанию](#содержание)

---

#### 1.4. Обновить refreshToken

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/refresh/refresh -H "Content-Type: application/json" -d "{\"refreshToken\":\"REFRESH_TOKEN\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/refresh/refresh -H "Content-Type: application/json" -d "{\"refreshToken\":\"REFRESH_TOKEN\"}"
```

**Ответ (200 OK):**
```json
{
  "type": "Bearer",
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

[↑ К содержанию](#содержание)

---

#### 1.5. Получить информацию о текущем пользователе

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/auth/user/me -H "Authorization: Bearer TOKEN"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/auth/user/me -H "Authorization: Bearer TOKEN"
```

**Ответ (200 OK):**
```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "login": "player1"
}
```

[↑ К содержанию](#содержание)

---

#### 1.6. Ошибка регистрации (логин занят)

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"password123\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"password123\"}"
```

**Ответ (409 Conflict):**
```json
{
  "error": "REGISTRATION_FAILED",
  "message": "User already exists",
  "status": 409
}
```

[↑ К содержанию](#содержание)

---

#### 1.7. Ошибка логина (неверный пароль)

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"wrong_password\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"login\":\"player1\",\"password\":\"wrong_password\"}"
```

**Ответ (401 Unauthorized):**
```json
{
  "error": "AUTHENTICATION_FAILED",
  "message": "Invalid password",
  "status": 401
}
```

[↑ К содержанию](#содержание)

---

#### 1.8. Ошибка авторизации (нет токена)

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/game/available
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/game/available
```

**Ответ (401 Unauthorized):**
```json
{
  "error": "Missing or invalid Authorization header"
}
```

[↑ К содержанию](#содержание)

---

### 2. Игра против компьютера

#### 2.1. Создать игру против компьютера

**Mac / Linux / cmd:**
```bash
curl -v -X POST "http://localhost:8080/api/game/create?vsComputer=true" -H "Authorization: Bearer TOKEN"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST "http://localhost:8080/api/game/create?vsComputer=true" -H "Authorization: Bearer TOKEN"
```

**Ответ (201 Created):**
```json
{
  "gameId": "770e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [0, 0, 0],
      [0, 0, 0],
      [0, 0, 0]
    ]
  },
  "status": "IN_PROGRESS",
  "currentPlayerId": "550e8400-e29b-41d4-a716-446655440000",
  "winnerId": null,
  "message": "Game created successfully"
}
```

[↑ К содержанию](#содержание)

---

#### 2.2. Сделать ход против компьютера

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN" -H "Content-Type: application/json" -d "{\"row\":1,\"col\":1}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN" -H "Content-Type: application/json" -d "{\"row\":1,\"col\":1}"
```

**Ответ (200 OK):**
```json
{
  "gameId": "770e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [0, 0, 2],
      [0, 1, 0],
      [0, 0, 0]
    ]
  },
  "status": "IN_PROGRESS",
  "currentPlayerId": "550e8400-e29b-41d4-a716-446655440000",
  "winnerId": null,
  "message": null
}
```

**Компьютер ответил ходом.**

[↑ К содержанию](#содержание)

---

#### 2.3. Получить состояние игры

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/game/GAME-ID -H "Authorization: Bearer TOKEN"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/game/GAME-ID -H "Authorization: Bearer TOKEN"
```

**Ответ (200 OK):**
```json
{
  "gameId": "770e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [0, 0, 2],
      [0, 1, 0],
      [0, 0, 0]
    ]
  },
  "status": "IN_PROGRESS",
  "currentPlayerId": "550e8400-e29b-41d4-a716-446655440000",
  "winnerId": null,
  "message": null
}
```

[↑ К содержанию](#содержание)

---

#### 2.4. Ошибка: ход в занятую клетку

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN" -H "Content-Type: application/json" -d "{\"row\":1,\"col\":1}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN" -H "Content-Type: application/json" -d "{\"row\":1,\"col\":1}"
```

**Ответ (400 Bad Request):**
```json
{
  "gameId": "770e8400-e29b-41d4-a716-446655440000",
  "field": null,
  "status": "ERROR",
  "currentPlayerId": null,
  "winnerId": null,
  "message": "Cell is already occupied"
}
```

[↑ К содержанию](#содержание)

---

### 3. Игра между двумя игроками

#### 3.1. Зарегистрировать второго игрока

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"login\":\"player2\",\"password\":\"password456\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"login\":\"player2\",\"password\":\"password456\"}"
```

[↑ К содержанию](#содержание)

---

#### 3.2. Логин player2

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"login\":\"player2\",\"password\":\"password456\"}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"login\":\"player2\",\"password\":\"password456\"}"
```

[↑ К содержанию](#содержание)

---

#### 3.3. Создать игру (player1)

**Mac / Linux / cmd:**
```bash
curl -v -X POST "http://localhost:8080/api/game/create?vsComputer=false" -H "Authorization: Bearer TOKEN_PLAYER1"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST "http://localhost:8080/api/game/create?vsComputer=false" -H "Authorization: Bearer TOKEN_PLAYER1"
```

**Ответ (201 Created):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [0, 0, 0],
      [0, 0, 0],
      [0, 0, 0]
    ]
  },
  "status": "WAITING_FOR_PLAYERS",
  "currentPlayerId": "550e8400-e29b-41d4-a716-446655440000",
  "winnerId": null,
  "message": "Game created successfully"
}
```

[↑ К содержанию](#содержание)

---

#### 3.4. Получить список доступных игр (player2)

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/game/available -H "Authorization: Bearer TOKEN_PLAYER2"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/game/available -H "Authorization: Bearer TOKEN_PLAYER2"
```

**Ответ (200 OK):**
```json
[
  {
    "gameId": "880e8400-e29b-41d4-a716-446655440000",
    "player1Id": "550e8400-e29b-41d4-a716-446655440000",
    "player2Id": null,
    "status": "WAITING_FOR_PLAYERS",
    "vsComputer": false
  }
]
```

[↑ К содержанию](#содержание)

---

#### 3.5. Присоединиться к игре (player2)

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/join -H "Authorization: Bearer TOKEN_PLAYER2"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/join -H "Authorization: Bearer TOKEN_PLAYER2"
```

**Ответ (200 OK):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [0, 0, 0],
      [0, 0, 0],
      [0, 0, 0]
    ]
  },
  "status": "IN_PROGRESS",
  "currentPlayerId": "550e8400-e29b-41d4-a716-446655440000",
  "winnerId": null,
  "message": "Successfully joined the game"
}
```

[↑ К содержанию](#содержание)

---

#### 3.6. Ход player1

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER1" -H "Content-Type: application/json" -d "{\"row\":0,\"col\":0}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER1" -H "Content-Type: application/json" -d "{\"row\":0,\"col\":0}"
```

**Ответ (200 OK):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [1, 0, 0],
      [0, 0, 0],
      [0, 0, 0]
    ]
  },
  "status": "IN_PROGRESS",
  "currentPlayerId": "660e8400-e29b-41d4-a716-446655440001",
  "winnerId": null,
  "message": null
}
```

[↑ К содержанию](#содержание)

---

#### 3.7. Ход player2

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER2" -H "Content-Type: application/json" -d "{\"row\":1,\"col\":1}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER2" -H "Content-Type: application/json" -d "{\"row\":1,\"col\":1}"
```

**Ответ (200 OK):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [1, 0, 0],
      [0, 2, 0],
      [0, 0, 0]
    ]
  },
  "status": "IN_PROGRESS",
  "currentPlayerId": "550e8400-e29b-41d4-a716-446655440000",
  "winnerId": null,
  "message": null
}
```

[↑ К содержанию](#содержание)

---

#### 3.8. Ошибка: не ваш ход

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER2" -H "Content-Type: application/json" -d "{\"row\":2,\"col\":2}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER2" -H "Content-Type: application/json" -d "{\"row\":2,\"col\":2}"
```

**Ответ (400 Bad Request):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": null,
  "status": "ERROR",
  "currentPlayerId": null,
  "winnerId": null,
  "message": "Not your turn"
}
```

[↑ К содержанию](#содержание)

---

#### 3.9. Ошибка: присоединиться к полной игре

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/join -H "Authorization: Bearer TOKEN_PLAYER3"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/join -H "Authorization: Bearer TOKEN_PLAYER3"
```

**Ответ (400 Bad Request):**
```json
{
  "gameId": null,
  "field": null,
  "status": "ERROR",
  "currentPlayerId": null,
  "winnerId": null,
  "message": "Game already has two players"
}
```

[↑ К содержанию](#содержание)

---

#### 3.10. Ошибка: игрок не в игре

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/game/GAME-ID -H "Authorization: Bearer TOKEN_PLAYER3"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/game/GAME-ID -H "Authorization: Bearer TOKEN_PLAYER3"
```

**Ответ (403 Forbidden):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": null,
  "status": "FORBIDDEN",
  "currentPlayerId": null,
  "winnerId": null,
  "message": "You are not in this game"
}
```

[↑ К содержанию](#содержание)

---

### 4. Завершение игры

#### 4.1. Победа игрока

**Mac / Linux / cmd:**
```bash
curl -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER1" -H "Content-Type: application/json" -d "{\"row\":0,\"col\":2}"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X POST http://localhost:8080/api/game/GAME-ID/move -H "Authorization: Bearer TOKEN_PLAYER1" -H "Content-Type: application/json" -d "{\"row\":0,\"col\":2}"
```

**Ответ (200 OK):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [1, 1, 1],
      [0, 2, 0],
      [0, 0, 2]
    ]
  },
  "status": "FINISHED",
  "currentPlayerId": null,
  "winnerId": "550e8400-e29b-41d4-a716-446655440000",
  "message": null
}
```

[↑ К содержанию](#содержание)

---

#### 4.2. Ничья

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/game/GAME-ID -H "Authorization: Bearer TOKEN_PLAYER1"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/game/GAME-ID -H "Authorization: Bearer TOKEN_PLAYER1"
```

**Ответ (200 OK):**
```json
{
  "gameId": "880e8400-e29b-41d4-a716-446655440000",
  "field": {
    "matrix": [
      [1, 2, 1],
      [1, 2, 2],
      [2, 1, 1]
    ]
  },
  "status": "DRAW",
  "currentPlayerId": null,
  "winnerId": null,
  "message": null
}
```

[↑ К содержанию](#содержание)

---

### 5. История игр и таблица лидеров

#### 5.1. Получить историю завершённых игр

**Mac / Linux / cmd:**
```bash
curl -v -X GET http://localhost:8080/api/game/history -H "Authorization: Bearer TOKEN"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET http://localhost:8080/api/game/history -H "Authorization: Bearer TOKEN"
```

**Ответ (200 OK):**
```json
[
  {
    "gameId": "880e8400-e29b-41d4-a716-446655440000",
    "player1Id": "550e8400-e29b-41d4-a716-446655440000",
    "player2Id": "660e8400-e29b-41d4-a716-446655440001",
    "status": "FINISHED",
    "vsComputer": false
  }
]
```

[↑ К содержанию](#содержание)

---

#### 5.2. Получить таблицу лидеров

**Mac / Linux / cmd:**
```bash
curl -v -X GET "http://localhost:8080/api/game/leaderboard?limit=10" -H "Authorization: Bearer TOKEN"
```

**Windows (PowerShell):**
```powershell
curl.exe --% -v -X GET "http://localhost:8080/api/game/leaderboard?limit=10" -H "Authorization: Bearer TOKEN"
```

**Ответ (200 OK):**
```json
[
  {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "login": "player1",
    "wins": 5,
    "losses": 2,
    "draws": 1,
    "winRatio": 0.7142857142857143
  },
  {
    "userId": "660e8400-e29b-41d4-a716-446655440001",
    "login": "player2",
    "wins": 3,
    "losses": 4,
    "draws": 0,
    "winRatio": 0.42857142857142855
  }
]
```

[↑ К содержанию](#содержание)

---

## Очистка базы данных

### Просмотр данных

**Mac / Linux / cmd:**
```bash
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "SELECT * FROM users;"
```

**Windows (PowerShell):**
```powershell
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "SELECT * FROM users;"
```

### Удаление конкретного пользователя

**Mac / Linux / cmd:**
```bash
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "DELETE FROM users WHERE login = 'player1';"
```

**Windows (PowerShell):**
```powershell
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "DELETE FROM users WHERE login = 'player1';"
```

### Удаление всех пользователей

**Mac / Linux / cmd:**
```bash
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "DELETE FROM users;"
```

**Windows (PowerShell):**
```powershell
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "DELETE FROM users;"
```

### Удаление всех игр

**Mac / Linux / cmd:**
```bash
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "DELETE FROM games;"
```

**Windows (PowerShell):**
```powershell
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "DELETE FROM games;"
```

### Полная очистка всех таблиц

**Mac / Linux / cmd:**
```bash
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "TRUNCATE TABLE games, game_fields, users, user_roles CASCADE;"
```

**Windows (PowerShell):**
```powershell
docker exec -it tictactoe_db psql -U postgres -d tictactoe_db -c "TRUNCATE TABLE games, game_fields, users, user_roles CASCADE;"
```

### Полное удаление контейнера и данных

**Mac / Linux / cmd:**
```bash
docker compose down -v
docker compose up -d
```

**Windows (PowerShell):**
```powershell
docker compose down -v
docker compose up -d
```

[↑ К содержанию](#содержание)