package com.example.ressourceRelationnelle.Modele;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Type_Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String libelle;

    public Type_Ressource(){}

    public Type_Ressource(int id, String libelle){
        this.id = id;
        this.libelle = libelle;
    }

    public int getId(){return this.id;}
    public String getLibelle(){return this.libelle;}

    public void setLibelle(String libelle){this.libelle = libelle; }
}
