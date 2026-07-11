package com.careernav.controller;

import com.careernav.model.User;
import com.careernav.repository.UserRepository;
import com.careernav.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        String name = req.get("name");
        String rollNumber = req.get("rollNumber");
        String password = req.get("password");

        if (name == null || rollNumber == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Name, roll number and password are required."));
        }
        if (userRepository.existsByRollNumber(rollNumber)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Roll number already registered."));
        }

        User user = new User();
        user.setName(name);
        user.setRollNumber(rollNumber);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("student");
        userRepository.save(user);

        // Generate token and return
        String token = generateTokenForUser(user);
        return ResponseEntity.ok(Map.of(
            "token", token,
            "user", sanitizeUser(user)
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        String rollNumber = req.get("rollNumber");
        String password = req.get("password");

        User user = userRepository.findByRollNumber(rollNumber).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid roll number or password."));
        }

        String token = generateTokenForUser(user);
        return ResponseEntity.ok(Map.of(
            "token", token,
            "user", sanitizeUser(user)
        ));
    }

    // Helper: create a lightweight UserDetails-like object for token generation
    private String generateTokenForUser(User user) {
        var userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getRollNumber())
                .password(user.getPassword())
                .roles(user.getRole().toUpperCase())
                .build();
        return jwtUtils.generateToken(userDetails);
    }

    // Helper: return user without sensitive data
    private Map<String, Object> sanitizeUser(User user) {
        return Map.of(
            "id", user.getId(),
            "name", user.getName(),
            "rollNumber", user.getRollNumber(),
            "role", user.getRole()
        );
    }
}
