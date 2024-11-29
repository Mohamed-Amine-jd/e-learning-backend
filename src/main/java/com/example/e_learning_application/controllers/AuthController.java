package com.example.e_learning_application.controllers;

import com.example.e_learning_application.config.ApiResponse;
import com.example.e_learning_application.config.LoginRequest;
import com.example.e_learning_application.config.LoginResponse;
import com.example.e_learning_application.entities.User;
import com.example.e_learning_application.repositories.UserRepository;
import com.example.e_learning_application.services.UserService;
import com.example.e_learning_application.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        // Perform registration logic
        userService.saveUser(user);

        // Return a JSON response with a message
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }


    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            if (userService.verifyEmail(token)) {
                return ResponseEntity.ok("Email verified successfully!");
            }
            return ResponseEntity.status(400).body("Invalid or expired verification token.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error verifying email: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            userService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Password reset email sent.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending password reset email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            if (userService.resetPassword(token, newPassword)) {
                return ResponseEntity.ok("Password reset successfully.");
            }
            return ResponseEntity.status(400).body("Invalid or expired reset token.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error resetting password: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
            if (token != null) {
                Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    // Add userId in the response
                    LoginResponse loginResponse = new LoginResponse("Login successful!", token, user.getRole(), user.getId());
                    System.out.println("id:"+user.getId());
                    return ResponseEntity.ok(loginResponse);
                }
            }
            // Return error response if login fails
            LoginResponse errorResponse = new LoginResponse("Invalid email or password.", null, null, null);
            return ResponseEntity.status(400).body(errorResponse);
        } catch (Exception e) {
            // Handle unexpected errors
            LoginResponse errorResponse = new LoginResponse("Error during login: " + e.getMessage(), null, null, null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }



}