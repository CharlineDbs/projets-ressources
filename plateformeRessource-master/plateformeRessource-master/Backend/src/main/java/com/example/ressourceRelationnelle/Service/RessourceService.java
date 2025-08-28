package com.example.ressourceRelationnelle.Service;
import com.example.ressourceRelationnelle.DataAccess.RessourceRepository;
import com.example.ressourceRelationnelle.Modele.Ressource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RessourceService {
    @Autowired
    private RessourceRepository ressourceRepository;

    public List<Ressource> getAllRessource(){
        return ressourceRepository.findAll();
    }
    public Optional<Ressource> getRessourceById(Long id){return ressourceRepository.findById(id);}
    public Ressource createRessource(Ressource newRessource){return ressourceRepository.save(newRessource);}
    public Ressource updateRessource(Ressource updatedRessource){return ressourceRepository.save(updatedRessource);}
    public boolean deleteRessource(Long id) {
        Optional<Ressource> ressource = ressourceRepository.findById(id);
        if (ressource.isPresent()) {
            ressourceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
