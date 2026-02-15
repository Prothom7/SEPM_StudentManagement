package com.example.studentmanagement.service;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentServiceTest {

    @Test
    void getAllStudentsReturnsRepositoryData() {
        List<Student> store = new ArrayList<>();
        store.add(new Student(1L, "A", "B", "a@b.com"));
        StudentService studentService = new StudentService(createRepository(store));

        List<Student> result = studentService.getAllStudents();

        assertEquals(1, result.size());
    }

    @Test
    void saveStudentDelegatesToRepository() {
        List<Student> store = new ArrayList<>();
        StudentService studentService = new StudentService(createRepository(store));

        Student student = new Student(2L, "A", "B", "a@b.com");
        Student result = studentService.saveStudent(student);

        assertEquals(student, result);
        assertEquals(1, store.size());
    }

    @Test
    void deleteStudentDelegatesToRepository() {
        List<Student> store = new ArrayList<>();
        store.add(new Student(7L, "A", "B", "a@b.com"));
        StudentService studentService = new StudentService(createRepository(store));

        studentService.deleteStudent(7L);

        assertTrue(store.isEmpty());
    }

    private StudentRepository createRepository(List<Student> store) {
        return (StudentRepository) Proxy.newProxyInstance(
                StudentRepository.class.getClassLoader(),
                new Class[]{StudentRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findAll")) {
                        return store;
                    }
                    if (method.getName().equals("save")) {
                        Student student = (Student) args[0];
                        store.add(student);
                        return student;
                    }
                    if (method.getName().equals("deleteById")) {
                        Long id = (Long) args[0];
                        store.removeIf(student -> id.equals(student.getId()));
                        return null;
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }
}
