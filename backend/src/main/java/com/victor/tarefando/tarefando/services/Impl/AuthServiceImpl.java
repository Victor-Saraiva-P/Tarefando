package com.victor.tarefando.tarefando.services.Impl;


import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.dtos.user.LoginUserDTO;
import com.victor.tarefando.tarefando.dtos.user.UserDTO;
import com.victor.tarefando.tarefando.exceptions.user.*;
import com.victor.tarefando.tarefando.mappers.Mapper;
import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.repositories.UserRepository;
import com.victor.tarefando.tarefando.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Mapper<UserEntity, CreateUserDTO> createUserDTOMapper;
    private final Mapper<UserEntity, UserDTO> userDTOMapper;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, Mapper<UserEntity, CreateUserDTO> createUserDTOMapper, Mapper<UserEntity, UserDTO> userDTOMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.createUserDTOMapper = createUserDTOMapper;
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public void register(@Valid CreateUserDTO createUserDTO) {
        // Verifica se o e-mail já está cadastrado
        if (userRepository.findByEmail(createUserDTO.getEmail())
                .isPresent()) {
            throw new EmailAlreadyExistsException(createUserDTO.getEmail());
        }
        // Verifica se o username já está cadastrado
        if (userRepository.findByUsername(createUserDTO.getUsername())
                .isPresent()) {
            throw new UsernameAlreadyExistsException(createUserDTO.getUsername());
        }

        try {
            // Converte o dto para entidade
            UserEntity user = createUserDTOMapper.mapFrom(createUserDTO);

            // Encode da senha
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // salva
            userRepository.save(user);
        } catch (Exception e) {
            throw new UserOperationException("Erro ao registrar usuario. Por favor, tente novamente", e);
        }
    }


    @Override
    public UserDTO login(LoginUserDTO loginUserDTO) {
        UserEntity userFoundByEmail = userRepository.findByEmail(loginUserDTO.getEmail())
                .orElseThrow(() -> new EmailNotFoundException(loginUserDTO.getEmail()));

        // Verifica se a senha informada é a mesma que a senha cadastrada
        if (passwordEncoder.matches(loginUserDTO.getPassword(), userFoundByEmail.getPassword())) {
            return userDTOMapper.mapTo(userFoundByEmail);
        }
        // Senão encontrar a senha é inválida
        throw new InvalidPasswordException();
    }

}