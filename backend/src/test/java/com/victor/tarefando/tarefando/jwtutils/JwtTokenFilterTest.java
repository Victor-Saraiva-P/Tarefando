package com.victor.tarefando.tarefando.jwtutils;

import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenFilter - Teste de Filtragem")
class JwtTokenFilterTest {

    @Mock
    private TokenManager tokenManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        jwtTokenFilter = new JwtTokenFilter(userRepository, tokenManager);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("Cenários sem Token ou com Token Inválido")
    class NoTokenOrInvalidTokenTests {

        @Test
        @DisplayName("Deve continuar o filtro quando não há token")
        void shouldContinueFilterChain_whenNoToken() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn(null);

            // Act
            jwtTokenFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext()
                    .getAuthentication());
        }

        @Test
        @DisplayName("Deve continuar o filtro quando o formato do token é inválido")
        void shouldContinueFilterChain_whenInvalidTokenFormat() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Invalid-Format-Token");

            // Act
            jwtTokenFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext()
                    .getAuthentication());
        }
    }

    @Nested
    @DisplayName("Cenários com Token Válido mas Usuário Inválido")
    class ValidTokenButInvalidUserTests {

        @Test
        @DisplayName("Deve continuar o filtro quando o usuário não é encontrado")
        void shouldContinueFilterChain_whenUserNotFound() throws ServletException, IOException {
            // Arrange
            String token = "valid-token";
            UUID userId = UUID.randomUUID();

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(tokenManager.extractSubject(token)).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act
            jwtTokenFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext()
                    .getAuthentication());
        }

        @Test
        @DisplayName("Deve continuar o filtro quando o token é inválido para o usuário")
        void shouldContinueFilterChain_whenTokenInvalidForUser() throws ServletException, IOException {
            // Arrange
            String token = "valid-token";
            UUID userId = UUID.randomUUID();
            UserEntity user = mock(UserEntity.class);

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(tokenManager.extractSubject(token)).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(tokenManager.validateToken(token, user)).thenReturn(false);

            // Act
            jwtTokenFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext()
                    .getAuthentication());
        }
    }

    @Nested
    @DisplayName("Cenários com Autenticação Bem-Sucedida")
    class SuccessfulAuthenticationTests {

        @Test
        @DisplayName("Deve configurar autenticação quando token e usuário são válidos")
        void shouldSetAuthentication_whenTokenAndUserValid() throws ServletException, IOException {
            // Arrange
            String token = "valid-token";
            UUID userId = UUID.randomUUID();
            UserEntity user = mock(UserEntity.class);

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(tokenManager.extractSubject(token)).thenReturn(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(tokenManager.validateToken(token, user)).thenReturn(true);
            when(user.getRoleString()).thenReturn("USER");

            // Act
            jwtTokenFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            assertNotNull(authentication);
            assertEquals(user, authentication.getPrincipal());
            assertTrue(authentication.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority()
                            .equals("ROLE_USER")));
        }
    }

    @Nested
    @DisplayName("Cenários com Autenticação Existente")
    class ExistingAuthenticationTests {

        @Test
        @DisplayName("Não deve modificar autenticação quando já existe no contexto")
        void shouldNotModifyAuthentication_whenAuthenticationAlreadyExists() throws ServletException, IOException {
            // Arrange
            String token = "valid-token";
            Authentication existingAuth = mock(Authentication.class);
            SecurityContextHolder.getContext()
                    .setAuthentication(existingAuth);

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

            // Act
            jwtTokenFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);
            assertSame(existingAuth, SecurityContextHolder.getContext()
                    .getAuthentication());
            verifyNoInteractions(tokenManager);
        }
    }
}