package com.example.studentmanagement.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentRepositoryTest {

    @Test
    void repositoryExtendsJpaRepository() {
        assertTrue(StudentRepository.class.isInterface());
        assertTrue(JpaRepository.class.isAssignableFrom(StudentRepository.class));
    }
}
