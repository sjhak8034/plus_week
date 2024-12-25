package com.example.demo.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PasswordEncoderTest {
    private String rawPassword;
    private String rawPassword2;

    @BeforeEach
    void setUp() {
        this.rawPassword = "password";
        this.rawPassword2 = "password2";
    }

    @Test
    void encode() {
        String encodedPassword = PasswordEncoder.encode(rawPassword);
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    void matchesTrue() {
        String encodedPassword = PasswordEncoder.encode(rawPassword);
        assertTrue(PasswordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void matchesFalse() {
        String encodedPassword = PasswordEncoder.encode(rawPassword);
        assertFalse(PasswordEncoder.matches(rawPassword2, encodedPassword));
    }

    @Test
    void encodeDifferent(){
        String encodedPassword = PasswordEncoder.encode(rawPassword);
        String encodedPassword2 = PasswordEncoder.encode(rawPassword);
        assertNotEquals(encodedPassword, encodedPassword2);
    }


}