package com.victor.tarefando.tarefando.testutils;


import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.dtos.user.LoginUserDTO;
import com.victor.tarefando.tarefando.dtos.user.UserDTO;
import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.models.enums.Role;

public class UserTestDataUtil {

    public static CreateUserDTO criarCreateUserDto(String nome) {
        return CreateUserDTO.builder()
                .username(nome)
                .email(nome + "@example.com")
                .password("123456")
                .role(Role.USER)
                .build();
    }

    public static UserEntity criarUserEntity(String nome) {
        return UserEntity.builder()
                .username(nome)
                .email(nome + "@example.com")
                .password("123456")
                .role(Role.USER)
                .build();
    }

    public static UserDTO criarUserDTO(String nome) {
        return UserDTO.builder()
                .username(nome)
                .email(nome + "@example.com")
                .role(Role.USER)
                .build();
    }

    public static LoginUserDTO criarLoginUserDTO(String nome) {
        return LoginUserDTO.builder()
                .email(nome + "@example.com")
                .password("123456")
                .build();
    }
}
