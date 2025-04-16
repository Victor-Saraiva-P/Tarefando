package com.victor.tarefando.tarefando.controller;


import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.dtos.user.LoginUserDTO;
import com.victor.tarefando.tarefando.dtos.user.UserDTO;
import com.victor.tarefando.tarefando.jwtutils.TokenManager;
import com.victor.tarefando.tarefando.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final TokenManager tokenManager;

    public AuthController(AuthService authService, TokenManager tokenManager) {
        this.authService = authService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody CreateUserDTO createUserDTO) {
        this.authService.register(createUserDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        UserDTO userDTO = authService.login(loginUserDTO);

        // Gera o token JWT
        String token = tokenManager.generateToken(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getRole()
        );

        return ResponseEntity.ok(Map.of("token", token));
    }
}