package com.example.ressourceRelationnelle.Service;

import com.example.ressourceRelationnelle.DataAccess.Type_RelationRepository;
import com.example.ressourceRelationnelle.Modele.Type_Relation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Type_RelationService {

    @Autowired
    private Type_RelationRepository type_relationRepository;

    public List<Type_Relation> getAllType_Relation(){
        return type_relationRepository.findAll();
    }
}
