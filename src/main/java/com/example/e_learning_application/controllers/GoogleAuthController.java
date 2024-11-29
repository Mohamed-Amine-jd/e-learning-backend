// src/main/java/com/example/e_learning_application/controllers/GoogleAuthController.java
package com.example.e_learning_application.controllers;


import com.example.e_learning_application.config.LoginResponse;
import com.example.e_learning_application.entities.User;
import com.example.e_learning_application.security.JWTUtil;
import com.example.e_learning_application.services.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/callback")
    public ResponseEntity<?> googleCallback(@RequestBody String credential) {
        try {
            GoogleIdToken idToken = googleAuthService.verifyGoogleToken(credential);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Process or create the user
                User user = googleAuthService.processGoogleUser(payload);

                // Generate JWT token with role
                String jwt = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());

                // Create response with user details and token
                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
                response.put("user", user);
                response.put("role", user.getRole()); // Include the role
                response.put("message", "Google login successful");

                return ResponseEntity.ok(response);
            }

            return ResponseEntity.badRequest().body("Invalid Google token");
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error processing Google login: " + e.getMessage());
        }
    }

}