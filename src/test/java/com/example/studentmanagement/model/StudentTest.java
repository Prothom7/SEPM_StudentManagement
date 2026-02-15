package com.example.studentmanagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentTest {

    @Test
    void builderSetsFields() {
        Student student = Student.builder()
                .id(1L)
                .firstName("A")
                .lastName("B")
                .email("a@b.com")
                .build();

        assertEquals(1L, student.getId());
        assertEquals("A", student.getFirstName());
        assertEquals("B", student.getLastName());
        assertEquals("a@b.com", student.getEmail());
    }

    @Test
    void allArgsConstructorSetsFields() {
        Student student = new Student(2L, "C", "D", "c@d.com");

        assertEquals(2L, student.getId());
        assertEquals("C", student.getFirstName());
        assertEquals("D", student.getLastName());
        assertEquals("c@d.com", student.getEmail());
    }
}
