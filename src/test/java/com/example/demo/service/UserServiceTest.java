package com.example.demo.service;

import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    User user;
    UserRequestDto userRequestDto;

    @BeforeEach
    void setUp() {
        String email = "test@test.com";
        String nickName = "test";
        String password = "test";
        String role = "admin";
        String encodedPassword = PasswordEncoder.encode(password);
        Long userId = 1L;
        this.userRequestDto = new UserRequestDto(role, email, nickName, encodedPassword);
        this.user = userRequestDto.toEntity();
        user.setId(userId);
    }

    @Test
    void signupWithEmail() {
        //given
        Long userId = user.getId();
        UserResponseDto userResponseDto = new UserResponseDto(userId);
        //when
        when(userRepository.save(any(User.class))).thenReturn(user);
        //then
        //assertEquals(userResponseDto, userService.signupWithEmail(userRequestDto));
        //assertThat(userService.signupWithEmail(userRequestDto).getId()).isEqualTo(userResponseDto.getId());
        assertEquals(userResponseDto, userService.signupWithEmail(userRequestDto));

    }

    @Test
    void loginUser() {
        //given
        String email = "test@test.com";
        String password = "test";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        Authentication authentication = new Authentication(user.getId(), user.getRole());
        //when
        when(userRepository.findByEmail(email)).thenReturn(user);
        //then
        assertEquals(authentication, userService.loginUser(loginRequestDto));
    }

    @Test
    void testLoginUser_UserNotFound() {
        String email = "test@test.com";
        String password = "test";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(null);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.loginUser(loginRequestDto));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("유효하지 않은 사용자 이름 혹은 잘못된 비밀번호", exception.getReason());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        String email = "test@test.com";
        String password = "test2";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(user);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.loginUser(loginRequestDto));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("유효하지 않은 사용자 이름 혹은 잘못된 비밀번호", exception.getReason());
    }

}