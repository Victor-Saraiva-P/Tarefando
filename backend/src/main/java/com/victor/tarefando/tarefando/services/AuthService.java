package com.victor.tarefando.tarefando.services;

import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.dtos.user.LoginUserDTO;
import com.victor.tarefando.tarefando.dtos.user.UserDTO;

public interface AuthService {
    void register(CreateUserDTO createUserDTO);

    UserDTO login(LoginUserDTO loginUserDTO);

}
