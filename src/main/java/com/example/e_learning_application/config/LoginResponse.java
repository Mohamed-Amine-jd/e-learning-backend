package com.example.e_learning_application.config;
public class LoginResponse {
    private String message;
    private String token;
    private String role;  // Ajoutez un attribut pour le rôle

    // Constructeur
    public LoginResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    // Getters et setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
