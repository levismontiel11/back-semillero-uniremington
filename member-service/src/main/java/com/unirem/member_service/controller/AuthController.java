package com.unirem.member_service.controller;

import com.unirem.member_service.DTO.LoginRequest;
import com.unirem.member_service.DTO.LoginResponse;
import com.unirem.member_service.DTO.RegisterRequest;
import com.unirem.member_service.DTO.UserDTO;
import com.unirem.member_service.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);

        Cookie cookie = new Cookie("jwt", loginResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        cookie.setAttribute("SameSite", "None");

        response.addCookie(cookie);

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        UserDTO createdUser = authService.register(request);
        return ResponseEntity.ok("User create successfully");
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUser(@CookieValue(name = "jwt", required = false) String token) {
        if (token == null) {
            return ResponseEntity.status(401).build();
        }

        UserDTO user = authService.getUserFromToken(token);
        return ResponseEntity.ok(user);
    }

}   
