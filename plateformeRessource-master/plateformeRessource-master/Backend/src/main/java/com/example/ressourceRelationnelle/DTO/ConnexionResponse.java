package com.example.ressourceRelationnelle.DTO;

import com.example.ressourceRelationnelle.Modele.Utilisateur;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnexionResponse {
    private Long id;
    private String email;
    private String pseudo;
    private String codepostal;
    private String ville;
    private Integer anneenaissance;
    private String etatCivil;
    private Boolean actif;
    private String token;

    // Constructeur par défaut
    public ConnexionResponse() {}

    // Constructeur avec Utilisateur
    public ConnexionResponse(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.email = utilisateur.getEmail();
        this.pseudo = utilisateur.getPseudo();
        this.codepostal = utilisateur.getCodepostal();
        this.ville = utilisateur.getVille();
        this.anneenaissance = utilisateur.getAnneenaissance();
        this.etatCivil = utilisateur.getEtatcivil();
        this.actif = utilisateur.getActif();
    }

    public ConnexionResponse(String token, Utilisateur utilisateur){
        this.token = token;
        this.id = utilisateur.getId();
        this.pseudo = utilisateur.getPseudo();
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPseudo() { return pseudo; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }

    public String getCodepostal() { return codepostal; }
    public void setCodepostal(String codepostal) { this.codepostal = codepostal; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public Integer getAnneenaissance() { return anneenaissance; }
    public void setAnneenaissance(Integer anneenaissance) { this.anneenaissance = anneenaissance; }

    public String getEtatCivil() { return etatCivil; }
    public void setEtatCivil(String etatCivil) { this.etatCivil = etatCivil; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

    public String getToken(){return this.token;}
    public void setToken(String token){this.token = token;}
}