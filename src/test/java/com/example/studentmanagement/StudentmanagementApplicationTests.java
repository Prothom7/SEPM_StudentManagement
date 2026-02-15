package com.example.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentmanagementApplicationTests {

    @Test
    void mainClassHasSpringBootApplicationAnnotation() {
        boolean hasAnnotation = StudentmanagementApplication.class
                .isAnnotationPresent(SpringBootApplication.class);
        assertTrue(hasAnnotation);
    }
}
