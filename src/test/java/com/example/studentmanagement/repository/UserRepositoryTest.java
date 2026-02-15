package com.example.studentmanagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {

    @Test
    void repositoryExtendsJpaRepository() {
        assertTrue(UserRepository.class.isInterface());
        assertTrue(JpaRepository.class.isAssignableFrom(UserRepository.class));
    }

    @Test
    void findByUsernameSignatureIsPresent() throws Exception {
        Method method = UserRepository.class.getMethod("findByUsername", String.class);

        assertEquals(Optional.class, method.getReturnType());
    }
}
