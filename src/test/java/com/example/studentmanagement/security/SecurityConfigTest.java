package com.example.studentmanagement.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Proxy;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityConfigTest {

    @Test
    void passwordEncoderIsBcrypt() {
        SecurityConfig config = new SecurityConfig(createUserDetailsService());

        PasswordEncoder encoder = config.passwordEncoder();

        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    void authenticationProviderIsConfigured() {
        SecurityConfig config = new SecurityConfig(createUserDetailsService());

        DaoAuthenticationProvider provider = config.authenticationProvider();

        assertNotNull(provider);
        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
    }

    private CustomUserDetailsService createUserDetailsService() {
        var repository = (com.example.studentmanagement.repository.UserRepository) Proxy.newProxyInstance(
                com.example.studentmanagement.repository.UserRepository.class.getClassLoader(),
                new Class[]{com.example.studentmanagement.repository.UserRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByUsername")) {
                        return Optional.empty();
                    }
                    if (method.getName().equals("save")) {
                        return args[0];
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );

        return new CustomUserDetailsService(repository);
    }
}
