package com.victor.tarefando.tarefando.jwtutils;

import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.models.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TokenManager - Validação de Tokens")
class TokenManagerTest {

    @Autowired
    private TokenManager tokenManager;

    private String secret;
    private UUID userId;
    private String email;
    private String username;
    private Role role;

    @BeforeEach
    void setUp() throws Exception {
        // Configuração comum para os testes
        userId = UUID.randomUUID();
        email = "test@example.com";
        username = "testUser";
        role = Role.USER;

        // Obtenção do segredo via reflexão
        Field secretField = TokenManager.class.getDeclaredField("SECRET");
        secretField.setAccessible(true);
        secret = (String) secretField.get(tokenManager);
    }

    // Metodo utilitário para criar tokens personalizados
    private String createCustomToken(Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + 60000); // válido por 1 minuto

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    static class DummyUserEntity extends UserEntity {
        private final String email;
        private final Role role;

        public DummyUserEntity(String email, Role role) {
            this.email = email;
            this.role = role;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getRoleString() {
            return role != null ? role.toString() : null;
        }
    }

    @Nested
    @DisplayName("Tokens Válidos")
    class ValidTokenTests {

        @Test
        @DisplayName("Deve validar token com dados corretos")
        void shouldValidateToken_whenAllClaimsMatch() {
            String token = tokenManager.generateToken(userId, username, email, role);
            DummyUserEntity user = new DummyUserEntity(email, role);

            assertTrue(tokenManager.validateToken(token, user));
        }
    }

    @Nested
    @DisplayName("Tokens com Claims Inválidas")
    class InvalidClaimsTests {

        @Test
        @DisplayName("Deve falhar quando email não corresponde")
        void shouldFailValidation_whenEmailMismatch() {
            String token = tokenManager.generateToken(userId, username, "mismatch@example.com", role);
            DummyUserEntity user = new DummyUserEntity("another@example.com", role);

            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando role não corresponde")
        void shouldFailValidation_whenRoleMismatch() {
            String token = tokenManager.generateToken(userId, username, email, Role.ADMIN);
            DummyUserEntity user = new DummyUserEntity(email, Role.USER);

            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando email está vazio")
        void shouldFailValidation_whenEmptyEmailMismatch() {
            String token = createCustomToken(new HashMap<>() {{
                put("username", username);
                put("email", "");
                put("role", role.toString());
            }});

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando role está vazia")
        void shouldFailValidation_whenEmptyRoleMismatch() {
            String token = createCustomToken(new HashMap<>() {{
                put("username", username);
                put("email", email);
                put("role", "");
            }});

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }
    }

    @Nested
    @DisplayName("Tokens com Claims Ausentes ou Inválidas")
    class MissingOrInvalidClaimsTests {

        @Test
        @DisplayName("Deve falhar quando a claim role está ausente")
        void shouldFailValidation_whenRoleClaimMissing() {
            String token = createCustomToken(new HashMap<>() {{
                put("username", username);
                put("email", email);
                // role ausente
            }});

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando a claim email está ausente")
        void shouldFailValidation_whenEmailClaimMissing() {
            String token = createCustomToken(new HashMap<>() {{
                put("username", username);
                put("role", role.toString());
                // email ausente
            }});

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando a claim role é null")
        void shouldFailValidation_whenRoleClaimNull() {
            String token = createCustomToken(new HashMap<>() {{
                put("username", username);
                put("email", email);
                put("role", null);
            }});

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando a claim role tem tipo incorreto")
        void shouldFailValidation_whenRoleClaimHasWrongType() {
            String token = createCustomToken(new HashMap<>() {{
                put("username", username);
                put("email", email);
                put("role", 12345); // tipo incorreto
            }});

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }
    }

    @Nested
    @DisplayName("Tokens Expirados ou Malformados")
    class ExpiredOrMalformedTokenTests {

        @Test
        @DisplayName("Deve falhar quando o token está expirado")
        void shouldFailValidation_whenTokenExpired() {
            // Token expirado (data no passado)
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("email", email);
            claims.put("role", role.toString());

            Date issuedAt = new Date(System.currentTimeMillis() - 60000);
            Date expiration = new Date(System.currentTimeMillis() - 1000); // já expirado

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userId.toString())
                    .setIssuedAt(issuedAt)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(token, user));
        }

        @Test
        @DisplayName("Deve falhar quando o token está malformado")
        void shouldFailValidation_whenTokenMalformed() {
            String malformedToken = "abc.def.ghi"; // não é JWT válido

            DummyUserEntity user = new DummyUserEntity(email, role);
            assertFalse(tokenManager.validateToken(malformedToken, user));
        }
    }
}