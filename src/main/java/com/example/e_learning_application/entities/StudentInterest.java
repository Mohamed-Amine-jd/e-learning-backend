package com.example.e_learning_application.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "StudentInterest")
public class StudentInterest {

    @Id
    private String id;
    private String email;
    private List<String> interests;

    // Constructor
    public StudentInterest(String email, List<String> interests) {
        this.email = email;
        this.interests = interests;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
