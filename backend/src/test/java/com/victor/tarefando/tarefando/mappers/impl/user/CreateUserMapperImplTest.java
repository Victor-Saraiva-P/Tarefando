package com.victor.tarefando.tarefando.mappers.impl.user;

import com.victor.tarefando.tarefando.dtos.user.CreateUserDTO;
import com.victor.tarefando.tarefando.models.UserEntity;
import com.victor.tarefando.tarefando.testutils.UserTestDataUtil;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateUserMapperImplTest {

    private final ModelMapper modelMapper = new ModelMapper();
    private final CreateUserMapperImpl createUserMapper = new CreateUserMapperImpl(modelMapper);

    @Test
    void mapTo_deveConverterUserEntityParaCreateUserDTO() {
        // Given
        UserEntity userEntity = UserTestDataUtil.criarUserEntity("testUser");

        // When
        CreateUserDTO result = createUserMapper.mapTo(userEntity);

        // Then
        assertNotNull(result);
        assertEquals(userEntity.getUsername(), result.getUsername());
        assertEquals(userEntity.getEmail(), result.getEmail());
        assertEquals(userEntity.getRole(), result.getRole());
    }

    @Test
    void mapFrom_deveConverterCreateUserDTOParaUserEntity() {
        // Given
        CreateUserDTO createUserDTO = UserTestDataUtil.criarCreateUserDto("testUser");

        // When
        UserEntity result = createUserMapper.mapFrom(createUserDTO);

        // Then
        assertNotNull(result);
        assertEquals(createUserDTO.getUsername(), result.getUsername());
        assertEquals(createUserDTO.getEmail(), result.getEmail());
        assertEquals(createUserDTO.getRole(), result.getRole());
    }
}