package com.unirem.member_service.service;


import com.unirem.member_service.DTO.LoginRequest;
import com.unirem.member_service.DTO.LoginResponse;
import com.unirem.member_service.DTO.RegisterRequest;
import com.unirem.member_service.DTO.UserDTO;
import com.unirem.member_service.config.JwtUtil;
import com.unirem.member_service.entity.User;
import com.unirem.member_service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getUserId());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        return response;
    }

    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setRole("MEMBER");
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setValid(false);

        user = userRepository.save(user);

        return userToUserDTO(user);
    }

    public UserDTO getUserFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token missing");
        }

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userToUserDTO(user);
    }

    private UserDTO userToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
