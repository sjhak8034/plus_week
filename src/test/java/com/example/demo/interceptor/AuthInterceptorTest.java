package com.example.demo.interceptor;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.entity.Role;
import com.example.demo.exception.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthInterceptorTest {

    @Mock
    private HttpSession session;
    @InjectMocks
    private AuthInterceptor authInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testPreHandle_SessionNull() {
        request.setSession(null);
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authInterceptor.preHandle(request, response, new Object()));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    }

    @Test
    void testPreHandle_AuthenticationNull() {
        when(session.getAttribute(GlobalConstants.USER_AUTH)).thenReturn(null);
        request.setSession(session);
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authInterceptor.preHandle(request, response, new Object()));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

    }

    @Test
    void testPreHandle_Success() {
        when(session.getAttribute(GlobalConstants.USER_AUTH)).thenReturn(new Authentication(1L, Role.USER));
        request.setSession(session);
        assertTrue(authInterceptor.preHandle(request, response, new Object()));
    }
}