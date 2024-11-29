package com.example.e_learning_application.services;

import com.example.e_learning_application.entities.User;
import com.example.e_learning_application.repositories.UserRepository;
import com.example.e_learning_application.security.JWTUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleAuthService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final String clientId;
    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthService(UserRepository userRepository,
                             JWTUtil jwtUtil,
                             @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.clientId = clientId;
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        processOAuth2User(oauth2User);
        return oauth2User;
    }

    public User processGoogleUser(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        Optional<User> existingUserOpt = userRepository.findByEmail(email);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Get the role of the existing user
            String role = existingUser.getRole();

            // Send the user details and role back to the frontend
            return existingUser; // Return the existing user with their role
        } else {
            // Create a new user if the email does not exist
            return createGoogleUser(payload);
        }
    }


    private User createGoogleUser(GoogleIdToken.Payload payload) {
        User user = new User();
        user.setEmail(payload.getEmail());
        user.setFirstName((String) payload.get("given_name"));
        user.setLastName((String) payload.get("family_name"));
        user.setSpeciality("");
        user.setRole("");

        // Set default values for required fields
        user.setPhoneNumber("");
        return userRepository.save(user);
    }

    private void processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        userRepository.findByEmail(email)
                .orElseGet(() -> createUser(oauth2User));
    }

    private User createUser(OAuth2User oauth2User) {
        User user = new User();
        user.setEmail(oauth2User.getAttribute("email"));
        user.setFirstName(oauth2User.getAttribute("given_name"));
        user.setLastName(oauth2User.getAttribute("family_name"));
        user.setRole("");
        user.setPhoneNumber("");
        user.setSpeciality("");
        return userRepository.save(user);
    }

    public GoogleIdToken verifyGoogleToken(String idTokenString) {
        try {
            return verifier.verify(idTokenString);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying Google token", e);
        }
    }

    public String generateTokenForGoogle(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return jwtUtil.generateToken(email, user.getRole(), user.getId()); // Pass userId now
        }

        // If the user is not found, assign a default role
        return jwtUtil.generateToken(email, "USER", "defaultId"); // You can provide a default ID if needed
    }

}