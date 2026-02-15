package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentControllerTest {

        @Test
        void listStudentsShowsTeacherView() {
                List<Student> store = new ArrayList<>();
                store.add(new Student(1L, "A", "B", "a@b.com"));
                StudentController controller = new StudentController(createStudentService(store));
                Authentication auth = new UsernamePasswordAuthenticationToken(
                                "teacher",
                                "n/a",
                                List.of(new SimpleGrantedAuthority("ROLE_TEACHER"))
                );
                Model model = new ConcurrentModel();

                String view = controller.students(auth, model);

                assertEquals("students", view);
                assertTrue(model.containsAttribute("students"));
        }

        @Test
        void listStudentsShowsStudentView() {
                List<Student> store = new ArrayList<>();
                store.add(new Student(1L, "A", "B", "a@b.com"));
                StudentController controller = new StudentController(createStudentService(store));
                Authentication auth = new UsernamePasswordAuthenticationToken(
                                "student",
                                "n/a",
                                List.of(new SimpleGrantedAuthority("ROLE_STUDENT"))
                );
                Model model = new ConcurrentModel();

                String view = controller.students(auth, model);

                assertEquals("students-view", view);
                assertTrue(model.containsAttribute("students"));
        }

        @Test
        void newStudentFormRendersView() {
                StudentController controller = new StudentController(createStudentService(new ArrayList<>()));
                Model model = new ConcurrentModel();

                String view = controller.newStudentForm(model);

                assertEquals("create_student", view);
                assertTrue(model.containsAttribute("student"));
        }

        @Test
        void saveStudentRedirectsToList() {
                List<Student> store = new ArrayList<>();
                StudentController controller = new StudentController(createStudentService(store));

                Student student = new Student(2L, "A", "B", "a@b.com");
                String view = controller.saveStudent(student);

                assertEquals("redirect:/students", view);
                assertEquals(1, store.size());
        }

        @Test
        void deleteStudentRedirectsToList() {
                List<Student> store = new ArrayList<>();
                store.add(new Student(5L, "A", "B", "a@b.com"));
                StudentController controller = new StudentController(createStudentService(store));

                String view = controller.deleteStudent(5L);

                assertEquals("redirect:/students", view);
                assertTrue(store.isEmpty());
        }

        private StudentService createStudentService(List<Student> store) {
                StudentRepository studentRepository = (StudentRepository) Proxy.newProxyInstance(
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

                return new StudentService(studentRepository);
        }
}
