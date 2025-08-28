package com.example.ressourceRelationnelle.Service;

import com.example.ressourceRelationnelle.DataAccess.Categorie_RessourceRepository;
import com.example.ressourceRelationnelle.Modele.Categorie_Ressource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Categorie_RessourceService {
    @Autowired
    private Categorie_RessourceRepository categorie_ressourceRepository;

    public List<Categorie_Ressource> getAllCategorie_ressource(){
        return categorie_ressourceRepository.findAll();
    }
}
