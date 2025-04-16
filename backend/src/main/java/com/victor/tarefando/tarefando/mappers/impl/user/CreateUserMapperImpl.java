package com.victor.tarefando.tarefando.mappers.impl.user;


import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.mappers.Mapper;
import com.victor.tarefando.tarefando.models.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateUserMapperImpl implements Mapper<UserEntity, CreateUserDTO> {
    private final ModelMapper modelMapper;

    @Override
    public CreateUserDTO mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, CreateUserDTO.class);
    }

    @Override
    public UserEntity mapFrom(CreateUserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }
}
