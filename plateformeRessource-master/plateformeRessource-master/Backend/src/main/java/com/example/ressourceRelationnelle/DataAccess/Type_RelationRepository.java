package com.example.ressourceRelationnelle.DataAccess;

import com.example.ressourceRelationnelle.Modele.Type_Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Type_RelationRepository extends JpaRepository<Type_Relation, Long> {
    public List<Type_Relation> findAll();
}
