package com.example.studentmanagement.security;

import com.example.studentmanagement.model.User;
import com.example.studentmanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsernameBuildsSpringUser() {
        Map<String, User> store = new HashMap<>();
        store.put("alice", new User(1L, "alice", "encoded", "TEACHER"));
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(createRepository(store));

        UserDetails details = userDetailsService.loadUserByUsername("alice");

        assertEquals("alice", details.getUsername());
        assertEquals("encoded", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER")));
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        Map<String, User> store = new HashMap<>();
        CustomUserDetailsService userDetailsService = new CustomUserDetailsService(createRepository(store));

        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing"));
    }

    private UserRepository createRepository(Map<String, User> store) {
        return (UserRepository) Proxy.newProxyInstance(
                UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByUsername")) {
                        String username = (String) args[0];
                        return Optional.ofNullable(store.get(username));
                    }
                    if (method.getName().equals("save")) {
                        User user = (User) args[0];
                        store.put(user.getUsername(), user);
                        return user;
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }
}
