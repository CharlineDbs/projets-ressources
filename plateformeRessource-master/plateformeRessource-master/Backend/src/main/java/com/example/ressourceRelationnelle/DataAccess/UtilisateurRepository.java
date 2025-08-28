package com.example.ressourceRelationnelle.DataAccess;

import com.example.ressourceRelationnelle.Modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    public List<Utilisateur> findAll();
    public Utilisateur findByEmail(String email);

    @Query(value = "SELECT MAX(id) FROM utilisateur", nativeQuery = true)
    public Long findMaxId();

}
