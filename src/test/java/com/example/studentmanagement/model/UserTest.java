package com.example.studentmanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {

    @Test
    void gettersAndSettersWork() {
        User user = new User();
        user.setId(1L);
        user.setUsername("alice");
        user.setPassword("encoded");
        user.setRole("TEACHER");

        assertEquals(1L, user.getId());
        assertEquals("alice", user.getUsername());
        assertEquals("encoded", user.getPassword());
        assertEquals("TEACHER", user.getRole());
    }

    @Test
    void allArgsConstructorSetsFields() {
        User user = new User(2L, "bob", "secret", "STUDENT");

        assertEquals(2L, user.getId());
        assertEquals("bob", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("STUDENT", user.getRole());
    }
}
