package com.victor.tarefando.tarefando.services.impl;

import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.dtos.user.LoginUserDTO;
import com.victor.tarefando.tarefando.dtos.user.UserDTO;
import com.victor.tarefando.tarefando.exceptions.user.*;
import com.victor.tarefando.tarefando.mappers.Mapper;
import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.repositories.UserRepository;
import com.victor.tarefando.tarefando.services.Impl.AuthServiceImpl;
import com.victor.tarefando.tarefando.testutils.UserTestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Mapper<UserEntity, CreateUserDTO> createUserDTOMapper;

    @Mock
    private Mapper<UserEntity, UserDTO> userDTOMapper;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userRepository, passwordEncoder, createUserDTOMapper, userDTOMapper);
    }

    /// -------------------TESTES DO METODO REGISTER-------------------//
    @Test
    void registerDeveSalvarComSucessoUsuario() {
        // Arrange
        CreateUserDTO createUserDTO = UserTestDataUtil.criarCreateUserDto("testuser");
        UserEntity userEntity = UserTestDataUtil.criarUserEntity("testuser");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(createUserDTOMapper.mapFrom(any(CreateUserDTO.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        authService.register(createUserDTO);

        // Assert
        verify(userRepository).save(userEntity);
        verify(passwordEncoder).encode("123456");
    }

    @Test
    void registarDeveThrowEmailAlreadyExistsException() {
        // Arrange
        CreateUserDTO createUserDTO = UserTestDataUtil.criarCreateUserDto("existingUser");
        UserEntity existingUser = UserTestDataUtil.criarUserEntity("existingUser");

        when(userRepository.findByEmail("existingUser@example.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class, () -> authService.register(createUserDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registrarDeveThrowUsernameAlreadyExistsException() {
        // Arrange
        CreateUserDTO createUserDTO = UserTestDataUtil.criarCreateUserDto("existingUser");
        UserEntity existingUser = UserTestDataUtil.criarUserEntity("existingUser");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(createUserDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerDeveThrowUserOperationException() {
        // Arrange
        CreateUserDTO createUserDTO = UserTestDataUtil.criarCreateUserDto("testuser");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(createUserDTOMapper.mapFrom(any(CreateUserDTO.class))).thenThrow(new RuntimeException("Mapper error"));

        // Act & Assert
        assertThrows(UserOperationException.class, () -> authService.register(createUserDTO));
        verify(userRepository, never()).save(any());
    }

    /// -------------------TESTES DO METODO LOGIN-------------------//
    @Test
    void loginDeveRetornarUserDTOComSucesso() {
        // Arrange
        LoginUserDTO loginUserDTO = UserTestDataUtil.criarLoginUserDTO("testuser");

        UserEntity userEntity = UserTestDataUtil.criarUserEntity("testuser");
        userEntity.setPassword("encodedPassword");

        UserDTO expectedUserDTO = UserTestDataUtil.criarUserDTO("testuser");

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("123456", "encodedPassword")).thenReturn(true);
        when(userDTOMapper.mapTo(userEntity)).thenReturn(expectedUserDTO);

        // Act
        UserDTO result = authService.login(loginUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedUserDTO, result);
    }

    @Test
    void loginShouldThrowEmailNotFoundException() {
        // Arrange
        LoginUserDTO loginUserDTO = UserTestDataUtil.criarLoginUserDTO("nonExistentUser");

        when(userRepository.findByEmail("nonExistentUser@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmailNotFoundException.class, () -> authService.login(loginUserDTO));
    }

    @Test
    void loginShouldThrowInvalidPasswordException() {
        // Arrange
        LoginUserDTO loginUserDTO = UserTestDataUtil.criarLoginUserDTO("testuser");
        loginUserDTO.setPassword("wrongpassword");

        UserEntity userEntity = UserTestDataUtil.criarUserEntity("testuser");
        userEntity.setPassword("encodedPassword");

        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(InvalidPasswordException.class, () -> authService.login(loginUserDTO));
    }
}