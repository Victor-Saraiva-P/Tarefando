package com.victor.tarefando.tarefando.mappers.impl;

import com.victor.tarefando.tarefando.dto.UserDTO;
import com.victor.tarefando.tarefando.mappers.Mapper;
import com.victor.tarefando.tarefando.models.UserEntity;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class UserMapperImpl implements Mapper<UserEntity, UserDTO> {
    private final ModelMapper modelMapper;

    @Override
    public UserDTO mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserEntity mapFrom(UserDTO userDTO) {
        return modelMapper.map(userDTO, UserEntity.class);
    }
}
