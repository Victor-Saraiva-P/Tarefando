package com.victor.tarefando.tarefando.dtos.user;

import com.victor.tarefando.tarefando.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;

    private String username;

    private String email;

    private Role role = Role.USER; // valor padrão
}
