package com.example.ressourceRelationnelle.Controller;
import com.example.ressourceRelationnelle.Modele.Type_Ressource;
import com.example.ressourceRelationnelle.Service.Type_RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8081") // <-- autorise ton frontend
@RestController
@RequestMapping("/api/type_ressource")
public class Type_RessourceController {
    @Autowired
    private  Type_RessourceService type_ressourceService;

    @GetMapping
    public List<Type_Ressource> getAllType_Ressource(){
        return type_ressourceService.getAllType_ressource();
    }

}
