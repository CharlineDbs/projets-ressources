package com.example.ressourceRelationnelle.Controller;

import com.example.ressourceRelationnelle.Modele.Categorie_Ressource;
import com.example.ressourceRelationnelle.Service.Categorie_RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8081") // <-- autorise ton frontend
@RestController
@RequestMapping("/api/categorie_ressource")
public class Categorie_RessourceController {
    @Autowired
    private  Categorie_RessourceService categorie_ressourceService;

    @GetMapping
    public List<Categorie_Ressource> getAllCategorie_Ressource(){
        return categorie_ressourceService.getAllCategorie_ressource();
    }

}
