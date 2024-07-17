package com.brainx.core.controller;

import com.brainx.core.entity.User;
import com.brainx.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userService.loginUser(username, password);
        if (user != null) {
            // Generate JWT token and return it in response (implementation omitted for brevity)
            String token = generateJwtToken(user);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    private String generateJwtToken(User user) {
        // JWT token generation logic goes here
        return "dummy-token"; // Placeholder
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
            // Invalidate JWT token or session
        return ResponseEntity.ok("Logout successful");
    }
}
