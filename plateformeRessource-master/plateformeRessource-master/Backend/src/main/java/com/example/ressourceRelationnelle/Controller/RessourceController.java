package com.example.ressourceRelationnelle.Controller;

import com.example.ressourceRelationnelle.Modele.Ressource;
import com.example.ressourceRelationnelle.Service.RessourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081") // <-- autorise ton frontend
@RestController
@RequestMapping("/api/ressources")
public class RessourceController {
    @Autowired
    private RessourceService ressourceService;

    @GetMapping
    public List<Ressource> getAllRessource(){
        return ressourceService.getAllRessource();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ressource> getRessourceById(@PathVariable Long id){
        Optional<Ressource> ressource = ressourceService.getRessourceById(id);
        return ressource
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<Ressource> createRessource(@RequestBody Ressource newRessource){
        Ressource saved = ressourceService.createRessource(newRessource);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ressource> upDateRessource(@PathVariable Long id, @RequestBody Ressource updatedRessource){
        return ressourceService.getRessourceById(id)
                .map(ressource -> {
                    ressource.setTitre(updatedRessource.getTitre());
                    ressource.setContenu(updatedRessource.getContenu());
                    ressource.setDateCreation(updatedRessource.getDateCreation());
                    ressource.setValide(updatedRessource.isValide());
                    ressource.setSuspendu(updatedRessource.isSuspendu());
                    ressource.setUtilisateur(updatedRessource.getUtilisateur());
                    ressource.setCategorie_ressource(updatedRessource.getCategorie_ressource());
                    ressource.setType_ressource(updatedRessource.getType_ressource());
                    ressource.setType_relation(updatedRessource.getType_relation());
                    Ressource saved = ressourceService.updateRessource(ressource);
                    return ResponseEntity.ok(saved);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRessource(@PathVariable Long id) {
        boolean deleted = ressourceService.deleteRessource(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }



}
