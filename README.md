# s21_Тic-Tac-Toe_Game_REST_API

## Описание

REST API для игры Крестики-Нолики с Minimax AI для хода компьютера + Spring Boot. Основной акцент - на чистой архитектуре: доменная модель не зависит от фреймворков, бизнес-логика отделена от веб-слоя и хранения данных. Использованы паттерны Repository, Mapper, Adapter. Иммутабельные объекты Game и GameField гарантируют потокобезопасность. REST API возвращает состояние игры в JSON, поддерживает создание игры и выполнение ходов с валидацией.
#
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

#
1. Стек: 
 - Java 18: современные фичи records, varargs
 - Spring Boot (без Web для бизнес-слоя)
 - Веб-слой	Spring MVC (REST Controllers)
 - Сериализация	Jackson (JSON)
 - Хранилище In-memory (ConcurrentHashMap)
 - Сборка Gradle: Kotlin DSL
 - REST API: реализовано два эндпоинта - создание игры (POST /game) и выполнение хода (POST /game/{gameId}). API использует UUID для идентификации игр, возвращает HTTP статусы (201, 200, 400, 404) и отдаёт полное состояние игры в JSON формате после каждого действия (клиент получает состояние поля 3x3, статус игры (IN_PROGRESS/WIN_PLAYER/WIN_COMPUTER/DRAW) и gameId для последующих запросов). Обработка ошибок через кастомные исключения с @ResponseStatus. 

#
2. Чистая гексагональная архитектура:
 - Domain layer - независимая бизнес-логика (нет Spring-аннотаций!)
 - Application/Service layer - оркестрация
 - Infrastructure/Data layer - репозитории, мапперы
 - Web/Presentation layer - REST API

#
3. Иммутабельность (Immutability):
 - Класс Game - все поля final
 - withMove() создаёт новый объект вместо изменения существующего
 - GameField.copy() - защита от побочных эффектов

#
4. Принцип единственной ответственности (SRP):
 - GameRules - только проверка победителя
 - MinimaxGameProcessor - только расчёт хода ИИ
 - GameDataMapper - только преобразование Entity ↔ Domain
 - GameWebMapper - только Domain ↔ DTO

#
5. Паттерны проектирования:
 - Mapper Pattern - разделение моделей для разных слоёв
 - Adapter Pattern - GameServiceAdapter адаптирует репозиторий к сервису
 - Strategy Pattern - GameProcessor может иметь разные реализации (Minimax, Random и т.д.)
 - Repository Pattern - абстракция над хранилищем данных
 - Dependency Injection - через Spring или ручную (AppConfig)

#
6. Алгоритмическая составляющая:
 - Minimax с альфа-бета отсечением - AI для хода компьютера
 - Оценка состояний с учётом глубины (предпочтение быстрых побед)

#
7. Чистый код:
 - Имена методов говорят сами за себя (isValidMove, makeComputerMove)
 - Константы вынесены в DomainConstants
 - Копирование матриц вместо прямых ссылок
