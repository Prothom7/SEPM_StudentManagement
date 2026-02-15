package com.example.studentmanagement.service;

import com.example.studentmanagement.model.User;
import com.example.studentmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserServiceTest {

    @Test
    void saveUserEncodesPassword() {
        Map<String, User> store = new HashMap<>();
        UserService userService = new UserService(createRepository(store), createEncoder());

        User user = new User(null, "alice", "plain", "TEACHER");
        User saved = userService.saveUser(user);

        assertEquals("encoded:plain", saved.getPassword());
        assertEquals(1, store.size());
    }

    @Test
    void findByUsernameReturnsUserWhenPresent() {
        Map<String, User> store = new HashMap<>();
        UserService userService = new UserService(createRepository(store), createEncoder());
        store.put("alice", new User(1L, "alice", "encoded:plain", "TEACHER"));

        User result = userService.findByUsername("alice");

        assertEquals("alice", result.getUsername());
    }

    @Test
    void findByUsernameReturnsNullWhenAbsent() {
        Map<String, User> store = new HashMap<>();
        UserService userService = new UserService(createRepository(store), createEncoder());

        User result = userService.findByUsername("missing");

        assertNull(result);
    }

    private UserRepository createRepository(Map<String, User> store) {
        return (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("save")) {
                        User user = (User) args[0];
                        store.put(user.getUsername(), user);
                        return user;
                    }
                    if (method.getName().equals("findByUsername")) {
                        String username = (String) args[0];
                        return Optional.ofNullable(store.get(username));
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }

    private PasswordEncoder createEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return "encoded:" + rawPassword;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encode(rawPassword).equals(encodedPassword);
            }
        };
    }
}
