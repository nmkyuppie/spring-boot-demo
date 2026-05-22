package com.xcelevate.demo;

import com.xcelevate.demo.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserApiControllerTest {

    @Test
    public void testUserEntityLombokBuilderAndSetters() {
        User user = User.builder()
                .id(100L)
                .name("Jane Doe")
                .email("jane@example.com")
                .role("MANAGER")
                .active(true)
                .build();

        assertEquals(100L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("MANAGER", user.getRole());
        assertTrue(user.getActive());

        // Test setters
        user.setActive(false);
        assertFalse(user.getActive());
        
        user.setName("Jane Smith");
        assertEquals("Jane Smith", user.getName());
    }
}
