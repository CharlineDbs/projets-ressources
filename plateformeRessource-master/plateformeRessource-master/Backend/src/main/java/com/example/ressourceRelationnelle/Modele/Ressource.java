package com.example.ressourceRelationnelle.Modele;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private LocalDateTime datecreation;
    private String contenu;
    private boolean valide;
    private boolean suspendu;

    //-- Catégorie ressource
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur = new Utilisateur();

    //-- Catégorie ressource
    @ManyToOne
    @JoinColumn(name = "categorie_ressource_id")
    private Categorie_Ressource categorie_ressource = new Categorie_Ressource();

    //-- Type ressource
    @ManyToOne
    @JoinColumn(name = "type_ressource_id")
    private Type_Ressource type_ressource = new Type_Ressource();

    //-- liste des types de relation
    @ManyToMany
    @JoinTable(
            name = "ressource_type_relation",
            joinColumns = @JoinColumn(name = "ressource_id"),
            inverseJoinColumns = @JoinColumn(name = "type_relation_id")
    )
    private Set<Type_Relation> type_relation = new HashSet<>();


    //-- CONSTRUCTOR
    public Ressource(){}
    public Ressource(Long id, String titre){
        this.id = id;
        this.titre = titre;
    }

    //-- GETTER / SETTER
    public Long getId(){return this.id;}
    public String getTitre(){return this.titre;}
    public LocalDateTime getDateCreation(){return this.datecreation;}
    public boolean isValide() {return valide;}
    public boolean isSuspendu() {return suspendu;}
    public String getContenu(){return this.contenu;}
    public Type_Ressource getType_ressource(){return this.type_ressource;}
    public Utilisateur getUtilisateur(){return this.utilisateur;}
    public Categorie_Ressource getCategorie_ressource(){return this.categorie_ressource;}
    public Set<Type_Relation> getType_relation(){return this.type_relation;}

    public void setId(Long id) {this.id = id;}
    public void setDateCreation(LocalDateTime dateCreation) {this.datecreation = dateCreation;}
    public void setTitre(String titre){this.titre = titre;}
    public void setContenu(String contenu) {this.contenu = contenu;}
    public void setValide(boolean valide) {this.valide = valide;}
    public void setSuspendu(boolean suspendu) {this.suspendu = suspendu;}
    public void setUtilisateur(Utilisateur utilisateur) {this.utilisateur = utilisateur;}
    public void setCategorie_ressource(Categorie_Ressource categorie_ressource) {this.categorie_ressource = categorie_ressource;}
    public void setType_ressource(Type_Ressource type_ressource) {this.type_ressource = type_ressource;}
    public void setType_relation(Set<Type_Relation> type_relation) {this.type_relation = type_relation;}
}
