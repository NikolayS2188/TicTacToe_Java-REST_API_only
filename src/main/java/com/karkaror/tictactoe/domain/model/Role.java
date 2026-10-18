package com.karkaror.tictactoe.domain.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Роли пользователя.
 * Реализует GrantedAuthority — интерфейс Spring Security,
 * который представляет право/роль для авторизации.
 * Важно:
 * Spring Security по умолчанию ожидает префикс ROLE_.
 * Если роль называется USER, то проверка hasRole("USER") ищет ROLE_USER.
 * Можно либо добавить префикс, либо использовать hasAuthority("USER").
 */
public enum Role implements GrantedAuthority {
    /** Роль обычного пользователя. */
    USER;

    /**
     * Возвращает имя роли.
     * Spring Security использует это значение для проверки прав.
     */
    @Override
    public String getAuthority() {
        return "ROLE_" + name(); // "USER"
    }
}

