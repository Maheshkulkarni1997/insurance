package org.safeguard.insurance.controller;

import org.safeguard.insurance.entitymodel.User;
import org.safeguard.insurance.repository.UserRepository;
import org.safeguard.insurance.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Save user to the database
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        // Find user by username
        User existingUser = userRepository.findByUsername(username);

        if (existingUser != null && passwordEncoder.matches(password, existingUser.getPassword())) {
            // Generate JWT token
            String token = jwtUtil.generateToken(username);
            return ResponseEntity.ok(token);
        }

        // Invalid credentials
        return ResponseEntity.status(401).body("Invalid credentials.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        // Find user by username
        User user = userRepository.findByUsername(username);

        if (user != null) {
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password updated successfully.");
        }

        // User not found
        return ResponseEntity.status(404).body("User not found.");
    }
}
