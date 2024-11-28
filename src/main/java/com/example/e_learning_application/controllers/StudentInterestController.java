package com.example.e_learning_application.controllers;

import com.example.e_learning_application.entities.StudentInterest;
import com.example.e_learning_application.services.StudentInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
public class StudentInterestController {

    @Autowired
    private StudentInterestService studentInterestService;

    @PostMapping("/save")
    public ResponseEntity<StudentInterest> saveInterests(@RequestBody StudentInterestRequest request) {
        StudentInterest studentInterest = studentInterestService.saveInterest(request.getEmail(), request.getInterests());
        return ResponseEntity.ok(studentInterest);
    }
}

// Create a DTO to capture both email and interests
class StudentInterestRequest {
    private String email;
    private List<String> interests;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
}
