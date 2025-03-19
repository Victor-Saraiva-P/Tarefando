package com.victor.tarefando.tarefando.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "O username é obrigatório")
    private String username;

    @NotBlank(message = "O e-mail é obrigatório")
    private String email;

    @NotBlank(message = "É necessário definir a senha")
    private String password;
}
