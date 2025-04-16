package com.victor.tarefando.tarefando.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.dtos.user.LoginUserDTO;
import com.victor.tarefando.tarefando.repositories.UserRepository;
import com.victor.tarefando.tarefando.testutils.UserTestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerTest {
    private static final String AUTH_BASE_URL = "/auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthController authController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveCarregarAuthController() {
        assertNotNull(authController, "O authController não deveria ser nulo!");
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    /// -------------------TESTES DO METODO REGISTER-------------------//
    @Test
    void deveRegistrarUsuario() throws Exception {
        CreateUserDTO userToRegister = UserTestDataUtil.criarCreateUserDto("UsuarioA");

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_BASE_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToRegister)))
                .andExpect(status().isCreated());
    }

    /// -------------------TESTES DO METODO LOGIN-------------------//
    @Test
    void deveLogarUsuario() throws Exception {
        CreateUserDTO userToLogin = adicionarUsuario("UsuarioA");
        LoginUserDTO userToLoginCredentials = LoginUserDTO.builder()
                .email(userToLogin.getEmail())
                .password(userToLogin.getPassword())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(AUTH_BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToLoginCredentials)))
                .andExpect(status().isOk());
    }

    /// -------------------------------MÉTODOS AUXILIARES-------------------------------//

    public CreateUserDTO adicionarUsuario(String nome) {
        CreateUserDTO createUserDTO = UserTestDataUtil.criarCreateUserDto(nome);

        authController.register(createUserDTO);

        return createUserDTO;
    }
}
