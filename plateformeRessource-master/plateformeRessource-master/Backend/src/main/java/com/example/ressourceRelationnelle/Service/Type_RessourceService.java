package com.example.ressourceRelationnelle.Service;
import com.example.ressourceRelationnelle.DataAccess.Type_RessourceRepository;
import com.example.ressourceRelationnelle.Modele.Type_Ressource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Type_RessourceService  {
    @Autowired
    private Type_RessourceRepository type_ressourceRepository;

    public List<Type_Ressource> getAllType_ressource(){
        return type_ressourceRepository.findAll();
    }
}
