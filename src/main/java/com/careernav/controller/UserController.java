package com.careernav.controller;

import com.careernav.model.User;
import com.careernav.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal User user,
                                           @RequestBody Map<String, Object> updates) {
        if (updates.containsKey("name")) user.setName((String) updates.get("name"));
        if (updates.containsKey("college")) user.setCollege((String) updates.get("college"));
        if (updates.containsKey("major")) user.setMajor((String) updates.get("major"));
        if (updates.containsKey("phone")) user.setPhone((String) updates.get("phone"));
        if (updates.containsKey("bio")) user.setBio((String) updates.get("bio"));
        if (updates.containsKey("gradYear")) user.setGradYear((String) updates.get("gradYear"));
        if (updates.containsKey("profilePicture")) user.setProfilePicture((String) updates.get("profilePicture"));

        // Social links
        if (updates.containsKey("github")) user.setGithub((String) updates.get("github"));
        if (updates.containsKey("linkedin")) user.setLinkedin((String) updates.get("linkedin"));
        if (updates.containsKey("website")) user.setWebsite((String) updates.get("website"));

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully.", "user", user));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal User user,
                                            @RequestBody Map<String, String> req) {
        String oldPassword = req.get("oldPassword");
        String newPassword = req.get("newPassword");

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Current password is incorrect."));
        }
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "New password must be at least 6 characters."));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
    }
}
