package com.example.ressourceRelationnelle.DTO;

public class ConnexionRequest {
    private String email;
    private String password;

    // Constructeur par défaut
    public ConnexionRequest() {}

    // Constructeur avec paramètres
    public ConnexionRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters et Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}