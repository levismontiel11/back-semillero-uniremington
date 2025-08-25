package com.unirem.member_service;

import com.unirem.member_service.DTO.LoginRequest;
import com.unirem.member_service.DTO.LoginResponse;
import com.unirem.member_service.DTO.RegisterRequest;
import com.unirem.member_service.DTO.UserDTO;
import com.unirem.member_service.config.JwtUtil;
import com.unirem.member_service.entity.User;
import com.unirem.member_service.repository.UserRepository;
import com.unirem.member_service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
        "eureka.client.enabled=false"
})
public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void successfulLogin() {
        LoginRequest request = new LoginRequest("user.test@unirem.edu.co", "userTest2025");

        User user = new User(1L, "Test User", "3333303939", "user.test@unirem.edu.co",
                "MEMBER", "encryptPass", true);

        when(userRepository.findByEmail("user.test@unirem.edu.co")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("userTest2025", "encryptPass")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString(), anyLong())).thenReturn("test-jwt");

        LoginResponse response = authService.login(request);
        assertNotNull(response);
        assertEquals("test-jwt", response.getToken());
    }

    @Test
    public void userNotFound() {
        LoginRequest request = new LoginRequest("user.notfound@unirem.edu.co", "1234");

        when(userRepository.findByEmail("user.notfound@unirem.edu.co")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    public void incorrectPassword() {
        LoginRequest request = new LoginRequest("user.incorrectpassword@unirem.edu.co", "incorrect");

        User user = new User(1L, "Test User", "3102345465", "user.incorrectpassword@unirem.edu.co",
                "MEMBER", "encryptPass", true);

        when(userRepository.findByEmail("user.incorrectpassword@unirem.edu.co")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("incorrect", "encryptPass")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    public void successfulRegister() {
        RegisterRequest request = new RegisterRequest("Test User", "3224225262",
                "new.user@unirem.edu.co", "1234", false);

        User savedUser = new User(1L, "Test User", "3224225262", "new.user@unirem.edu.co",
                "MEMBER", "encryptPass", false);

        when(userRepository.existsByEmail("new.user@unirem.edu.co")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("encryptPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(modelMapper.map(savedUser, UserDTO.class)).thenReturn(new UserDTO());

        UserDTO response = authService.register(request);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void registerWithExistingEmail() {
        RegisterRequest request = new RegisterRequest("Test User", "3405607890",
                "user.duplicated@unirem.edu.co", "1234", false);

        when(userRepository.existsByEmail("user.duplicated@unirem.edu.co")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
    }

    @Test
    public void getUserFromToken() {
        String token = "valid-jwt";

        User user = new User(1L, "Test User", "3302303030", "user.test@unirem.edu.co",
                "MEMBER", "encoryptPass", true);

        when(jwtUtil.extractEmail(token)).thenReturn("user.test@unirem.edu.co");
        when(userRepository.findByEmail("user.test@unirem.edu.co")).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(new UserDTO());

        UserDTO response = authService.getUserFromToken(token);

        assertNotNull(response);
    }

    @Test
    public void emptyToken() {
        assertThrows(RuntimeException.class, () -> authService.getUserFromToken(""));
    }
}
