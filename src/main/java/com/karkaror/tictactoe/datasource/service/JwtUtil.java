package com.karkaror.tictactoe.datasource.service;

import com.karkaror.tictactoe.domain.model.Role;
import com.karkaror.tictactoe.domain.service.JwtAuthentication;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Утилита для создания JwtAuthentication по claims.
 */
@Component
public class JwtUtil {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLES = "roles";

    public JwtAuthentication createAuthentication(Claims claims) {
        UUID userId = UUID.fromString(claims.get(CLAIM_USER_ID, String.class));

        @SuppressWarnings("unchecked")
        List<String> roleNames = claims.get(CLAIM_ROLES, List.class);

        List<Role> roles = roleNames == null
                ? List.of(Role.USER)
                : roleNames.stream().map(Role::valueOf).collect(Collectors.toList());

        return new JwtAuthentication(userId, roles, true);
    }

    public UUID extractUserId(Claims claims) {
        return UUID.fromString(claims.get(CLAIM_USER_ID, String.class));
    }
}
