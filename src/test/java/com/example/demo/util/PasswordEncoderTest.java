package com.example.demo.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PasswordEncoderTest {

    @Test
    void encode() {
        //given
        String rawPassword = "abide";


        //when
        String encodedPassword = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
        //then
        assertNotNull(encodedPassword);
    }

    @Test
    void matches() {
        //given
        String rawPassword = "abide";
        String encodedPassword = BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
        //when
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(),encodedPassword);
        //then

        assertTrue(result.verified);
    }


}