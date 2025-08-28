package com.example.ressourceRelationnelle.DataAccess;

import com.example.ressourceRelationnelle.Modele.Categorie_Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Categorie_RessourceRepository extends JpaRepository<Categorie_Ressource, Long> {
    public List<Categorie_Ressource> findAll();
}
