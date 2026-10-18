package com.karkaror.tictactoe.datasource.service;

import com.karkaror.tictactoe.domain.model.Role;
import com.karkaror.tictactoe.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Провайдер JWT: генерация, валидация, извлечение claims.
 */
@Component
public class JwtProvider {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";

    private final SecretKey accessSecret;
    private final SecretKey refreshSecret;
    private final long accessTtlMinutes;
    private final long refreshTtlDays;

    public JwtProvider(
            @Value("${jwt.access-secret}") String accessSecret,
            @Value("${jwt.refresh-secret}") String refreshSecret,
            @Value("${jwt.access-ttl-minutes:60}") long accessTtlMinutes,
            @Value("${jwt.refresh-ttl-days:30}") long refreshTtlDays) {
        // Секреты должны быть в base64 и достаточно длинными (>= 256 бит)
        this.accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));
        this.refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
        this.accessTtlMinutes = accessTtlMinutes;
        this.refreshTtlDays = refreshTtlDays;
    }

    /**
     * Генерирует accessToken.
     * В claims сохраняются кастомные:
     * userId (UUID пользователя как String)
     * roles (список ролей как List<String>)
     */
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(accessTtlMinutes, ChronoUnit.MINUTES);

        List<String> roles = user.roles().stream()
                .map(Role::name)
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(user.id().toString())              // sub = UUID пользователя
                .claim(CLAIM_USER_ID, user.id().toString()) // userId = UUID пользователя
                .claim(CLAIM_ROLES, roles)                  // roles = [USER]
                .issuedAt(Date.from(now))                   // iat - когда выпущен токен
                .expiration(Date.from(expiry))              // exp - время действия токена
                .signWith(accessSecret)                     // signature - подсчет HMAC-SHA256 с секретным ключом accessSecret от части JWT-строки: header + "." + payload
                .compact();                                 // собрать токен в строку
    }

    /** Генерирует refreshToken с UUID.
     * В claims сохраняются кастомные:
     * userId (UUID пользователя как String)
     * roles (список ролей как List<String>)
     */
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiry = now.plus(refreshTtlDays, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(user.id().toString())
                .claim(CLAIM_USER_ID, user.id().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(refreshSecret)
                .compact();
    }

    /** Валидирует accessToken. */
    public boolean validateAccessToken(String token) {
        return validate(token, accessSecret);
    }

    /** Валидирует refreshToken. */
    public boolean validateRefreshToken(String token) {
        return validate(token, refreshSecret);
    }

    /** Извлекает claims из accessToken. */
    public Claims getAccessClaims(String token) {
        return parse(token, accessSecret);
    }

    /** Извлекает claims из refreshToken. */
    public Claims getRefreshClaims(String token) {
        return parse(token, refreshSecret);
    }

    private boolean validate(String token, SecretKey key) {
        try {
            parse(token, key);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parse(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
