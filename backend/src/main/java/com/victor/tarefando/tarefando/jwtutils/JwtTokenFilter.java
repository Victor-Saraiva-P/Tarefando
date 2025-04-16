package com.victor.tarefando.tarefando.jwtutils;

import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final TokenManager tokenManager;

    private final UserRepository userRepository;

    public JwtTokenFilter(UserRepository userRepository, TokenManager tokenManager) {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = recoverToken(request);

        // Se há um token e ainda não existe autenticação no contexto...
        if (token != null && SecurityContextHolder.getContext()
                .getAuthentication() == null) {
            // Extrai o subject que foi definido como o id do usuário
            UUID userId = tokenManager.extractSubject(token);

            // Busca o usuário pelo id extraído
            UserEntity userEntity = userRepository.findById(userId)
                    .orElse(null);

            // Verifica se o usuário existe e se o token é válido em relação a esse usuário
            if (userEntity != null && tokenManager.validateToken(token, userEntity)) {
                // Extrai a role do token (para definição das authorities)
                String role = userEntity.getRoleString();
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + role));

                // Cria a autenticação e coloca no SecurityContext
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userEntity, null, authorities);
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.replace("Bearer ", "");
    }
}