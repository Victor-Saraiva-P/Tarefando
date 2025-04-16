package com.victor.tarefando.tarefando.jwtutils;

import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.models.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
public class TokenManager {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.token.expiration}")
    private Long EXPIRATION;

    public String generateToken(UUID id, String username, String email, Role role) {
        Map<String, Object> claims = new HashMap<>();

        // Define as claims
        claims.put("username", username);
        claims.put("email", email);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token, UserEntity expectedUser) {
        try {

            // Extrai as claims necessárias
            String emailFromToken = extractUserEmail(token);
            String roleFromToken = extractUserRole(token);

            // Verifica se o token não expirou e se as claims email e role estão corretas
            return emailFromToken.equals(expectedUser.getEmail()) &&
                    roleFromToken.equals(expectedUser.getRoleString()) &&
                    !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Se ocorrer a ExpiredJwtException, significa que o token está expirado.
            return true;
        }
    }


    // Extracts
    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public UUID extractSubject(String token) {
        // O subject foi definido como o id do usuário
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}