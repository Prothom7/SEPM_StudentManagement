package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.Student;
import com.example.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    // List all students; different view for teacher vs student
    @GetMapping
    public String students(Authentication auth, Model model) {
        model.addAttribute("students", studentService.getAllStudents());

        boolean isTeacher = auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER"));

        return isTeacher ? "students" : "students-view";
    }

    // Form to add new student - only teacher
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/new")
    public String newStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "create_student"; // create_student.html
    }

    // Save new student - only teacher
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public String saveStudent(@ModelAttribute Student student) {
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // Delete student - only teacher
    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }
}
