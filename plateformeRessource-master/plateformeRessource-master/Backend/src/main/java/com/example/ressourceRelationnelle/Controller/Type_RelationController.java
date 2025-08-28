package com.example.ressourceRelationnelle.Controller;
import com.example.ressourceRelationnelle.Modele.Type_Relation;
import com.example.ressourceRelationnelle.Service.Type_RelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081") // <-- autorise ton frontend
@RestController
@RequestMapping("/api/type_relation")
public class Type_RelationController {
    @Autowired
    private Type_RelationService type_relationService;

    @GetMapping
    public List<Type_Relation> getAllType_Relation(){return type_relationService.getAllType_Relation();}
}
