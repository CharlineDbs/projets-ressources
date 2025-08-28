package com.example.ressourceRelationnelle.DataAccess;
import com.example.ressourceRelationnelle.Modele.Type_Ressource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Type_RessourceRepository extends JpaRepository<Type_Ressource, Long> {
    public List<Type_Ressource> findAll();
}
