package com.victor.tarefando.tarefando.mappers.impl.user;

import com.victor.tarefando.tarefando.dtos.user.UserDTO;
import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.testutils.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMapperImplTest {

    private final ModelMapper modelMapper = new ModelMapper();
    private final UserMapperImpl userMapper = new UserMapperImpl(modelMapper);

    @Test
    void mapTo_DeveConverterUserEntityParaUserDTO() {
        // Given
        UserEntity userEntity = UserTestDataUtil.criarUserEntity("testUser");

        // When
        UserDTO result = userMapper.mapTo(userEntity);

        // Then
        assertNotNull(result);
        assertEquals(userEntity.getUsername(), result.getUsername());
        assertEquals(userEntity.getEmail(), result.getEmail());
        assertEquals(userEntity.getRole(), result.getRole());
    }

    @Test
    void mapFrom_DeveConverterUserDTOParaUserEntity() {
        // Given
        UserDTO userDTO = UserTestDataUtil.criarUserDTO("testUser");

        // When
        UserEntity result = userMapper.mapFrom(userDTO);

        // Then
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertEquals(userDTO.getEmail(), result.getEmail());
        assertEquals(userDTO.getRole(), result.getRole());
    }
}