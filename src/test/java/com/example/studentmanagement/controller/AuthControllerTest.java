package com.example.studentmanagement.controller;

import com.example.studentmanagement.model.User;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.lang.reflect.Proxy;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthControllerTest {

    @Test
    void showRegisterFormRendersView() {
        AuthController controller = new AuthController(createUserService(null));
        Model model = new ConcurrentModel();

        String view = controller.showRegisterForm(model);

        assertEquals("register", view);
        assertTrue(model.containsAttribute("user"));
    }

    @Test
    void registerUserRedirectsToLogin() {
        AtomicReference<User> savedRef = new AtomicReference<>();
        AuthController controller = new AuthController(createUserService(savedRef));

        User input = new User(null, "alice", "secret", "STUDENT");
        String view = controller.registerUser(input);

        assertEquals("redirect:/login", view);
        User saved = savedRef.get();
        assertNotNull(saved);
        assertEquals("alice", saved.getUsername());
        assertEquals("encoded:secret", saved.getPassword());
    }

    @Test
    void showLoginFormRendersView() {
        AuthController controller = new AuthController(createUserService(null));

        String view = controller.showLoginForm();

        assertEquals("login", view);
    }

    private UserService createUserService(AtomicReference<User> savedRef) {
        UserRepository userRepository = (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("save")) {
                        User user = (User) args[0];
                        if (savedRef != null) {
                            savedRef.set(user);
                        }
                        return user;
                    }
                    if (method.getName().equals("findByUsername")) {
                        return Optional.empty();
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );

        PasswordEncoder encoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "encoded:" + rawPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };

        return new UserService(userRepository, encoder);
    }
}
