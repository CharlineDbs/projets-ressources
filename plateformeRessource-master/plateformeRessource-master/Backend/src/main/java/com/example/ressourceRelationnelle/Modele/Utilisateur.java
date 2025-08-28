package com.example.ressourceRelationnelle.Modele;

import jakarta.persistence.*;

@Entity
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String pseudo;
    private String codepostal;
    private String ville;
    private String password;
    private Integer anneenaissance;
    private String etatcivil;
    private Boolean actif;

    public Utilisateur() {}

    // Ajout d'un constructeur avec ID
    public Utilisateur(Long id, String email, String pseudo, String codepostal,
                       String ville, String password, Integer anneenaissance,
                       String etatcivil) {
        this.id = id;
        this.email = email;
        this.pseudo = pseudo;
        this.codepostal = codepostal;
        this.ville = ville;
        this.password = password;
        this.anneenaissance = anneenaissance;
        this.etatcivil = etatcivil;
        this.actif = true;
    }

    // Getters
    public Long getId() { return this.id; }
    public String getEmail() { return this.email; }
    public String getPseudo() { return this.pseudo; }
    public String getCodepostal() { return this.codepostal; }
    public String getVille() { return this.ville; }
    public String getPassword() { return this.password; }
    public Integer getAnneenaissance() { return this.anneenaissance; }
    public String getEtatcivil() { return this.etatcivil; }
    public Boolean getActif() { return this.actif; }

    // Setters
    public void setEmail(String email) { this.email = email; }
    public void setPseudo(String pseudo) { this.pseudo = pseudo; }
    public void setCodepostal(String codepostal) { this.codepostal = codepostal; }
    public void setVille(String ville) { this.ville = ville; }
    public void setPassword(String password) { this.password = password; }
    public void setAnneenaissance(Integer anneenaissance) { this.anneenaissance = anneenaissance; }
    public void setEtatcivil(String etatcivil) { this.etatcivil = etatcivil; }
    public void setActif(Boolean actif) { this.actif = actif; }
}