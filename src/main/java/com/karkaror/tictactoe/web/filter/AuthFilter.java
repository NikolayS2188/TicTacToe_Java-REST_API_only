package com.karkaror.tictactoe.web.filter;

import com.karkaror.tictactoe.datasource.service.AuthServiceImpl;
import com.karkaror.tictactoe.datasource.service.JwtProvider;
import com.karkaror.tictactoe.domain.service.JwtAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр аутентификации.
 * Проверяет только заголовок Authorization.
 * Публичные эндпоинты пропускаются Spring Security (permitAll()).
 */
public class AuthFilter extends GenericFilterBean {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;
    private final AuthServiceImpl authService;

    public AuthFilter(JwtProvider jwtProvider, AuthServiceImpl authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String authHeader = httpRequest.getHeader(AUTH_HEADER);

        // Если заголовка нет — пропускаем дальше.
        // Spring Security сам решит: публичный это endpoint или нет.
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        // Валидация токена
        if (!jwtProvider.validateAccessToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Invalid access token\"}");
            return;
        }

        // Создание аутентификации
        JwtAuthentication authentication = authService.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
