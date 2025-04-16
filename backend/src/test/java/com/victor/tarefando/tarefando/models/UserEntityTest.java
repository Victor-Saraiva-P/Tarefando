package com.victor.tarefando.tarefando.models;

import com.victor.tarefando.tarefando.models.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


class UserEntityTest {
    @Test
    void testaDefaultRoleValue() {
        UserEntity user = new UserEntity();
        assertEquals(Role.USER, user.getRole());
    }

    @Test
    void testaGetAuthorities() {
        UserEntity user = new UserEntity();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void testaGetRoleString() {
        UserEntity user = new UserEntity();
        assertEquals("USER", user.getRoleString());

        user.setRole(Role.ADMIN);
        assertEquals("ADMIN", user.getRoleString());
    }
}