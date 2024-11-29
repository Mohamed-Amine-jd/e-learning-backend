package com.example.e_learning_application.services;

import com.example.e_learning_application.entities.User;
import com.example.e_learning_application.repositories.UserRepository;
import com.example.e_learning_application.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public User saveUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public boolean verifyEmail(String token) {
        String email = jwtUtil.extractUsername(token);
        if (email != null && !email.isEmpty()) {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public void sendPasswordResetEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String resetToken = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());

            String resetLink = "http://localhost:4200/reset-password?token=" + resetToken;

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Reset Your Password");
            message.setText("Click the following link to reset your password: " + resetLink);

            mailSender.send(message);
        } else {
            throw new RuntimeException("No user found with the provided email address.");
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        String email = jwtUtil.extractUsername(token);
        if (email != null && !email.isEmpty()) {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }


    public String login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                // Pass the user ID along with email and role to generate the token
                return jwtUtil.generateToken(email, user.getRole(), user.getId());
            }
        }
        return null;
    }





    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }


    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}