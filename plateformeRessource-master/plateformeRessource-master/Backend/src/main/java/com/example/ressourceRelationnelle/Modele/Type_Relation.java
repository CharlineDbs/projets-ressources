package com.example.ressourceRelationnelle.Modele;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Type_Relation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String libelle;

    @ManyToMany(mappedBy = "type_relation")
    private Set<Ressource> ressource = new HashSet<>();

    public Type_Relation(){}

    public Type_Relation(int id, String libelle){
        this.id = id;
        this.libelle = libelle;
    }

    public int getId(){return this.id;}
    public String getLibelle(){return this.libelle;}
    public void setLibelle(String libelle){this.libelle = libelle; }
}
