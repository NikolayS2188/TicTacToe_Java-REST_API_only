package com.karkaror.tictactoe.domain.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

/**
 * Кастомная реализация Authentication для JWT.
 * Хранит UUID пользователя и его роли.
 */
public class JwtAuthentication implements Authentication {

    private final UUID userId;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean authenticated;

    public JwtAuthentication(UUID userId,
                             Collection<? extends GrantedAuthority> authorities,
                             boolean authenticated) {
        this.userId = userId;
        this.authorities = authorities;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null; // JWT не хранит пароль
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userId.toString();
    }
}
