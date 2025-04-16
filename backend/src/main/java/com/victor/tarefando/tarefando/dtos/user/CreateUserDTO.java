package com.victor.tarefando.tarefando.dtos.user;

import com.victor.tarefando.tarefando.models.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

    @NotBlank(message = "O username é obrigatório")
    private String username;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "O password é obrigatória")
    @Size(min = 6, message = "A password deve ter no mínimo 6 caracteres")
    private String password;

    @NotNull(message = "A role do usuário é obrigatório")
    private Role role;
}