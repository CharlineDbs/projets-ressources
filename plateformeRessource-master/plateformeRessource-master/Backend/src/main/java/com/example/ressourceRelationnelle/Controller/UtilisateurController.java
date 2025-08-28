package com.example.ressourceRelationnelle.Controller;

import com.example.ressourceRelationnelle.DTO.JwtUtil;
import com.example.ressourceRelationnelle.Modele.Utilisateur;
import com.example.ressourceRelationnelle.Service.UtilisateurService;
import com.example.ressourceRelationnelle.DTO.ConnexionRequest;
import com.example.ressourceRelationnelle.DTO.ConnexionResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost:8081") // <-- autorise ton frontend


@RestController
@RequestMapping("/api/utilisateurs")
@Validated
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getAllUtilisateur() {
        try {
            return ResponseEntity.ok(utilisateurService.getAllUtilisateur());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération des utilisateurs");
        }
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@Valid @RequestBody Utilisateur utilisateur) {
        try {
            // Validation des données
            Map<String, String> erreurs = validerDonneesUtilisateur(utilisateur);
            if (!erreurs.isEmpty()) {
                return ResponseEntity.badRequest().body(erreurs);
            }

            Utilisateur nouvelUtilisateur = utilisateurService.inscription(utilisateur);
            return ResponseEntity.ok(nouvelUtilisateur);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'inscription");
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@Valid @RequestBody ConnexionRequest credentials) {
        try {
            String email = credentials.getEmail();
            String password = credentials.getPassword();

            if (email == null || password == null) {
                return ResponseEntity.badRequest().body("Email et mot de passe requis");
            }

            // Validation du format de l'email
            if (!validerEmail(email)) {
                return ResponseEntity.badRequest().body("Format d'email invalide");
            }

            Utilisateur utilisateur = utilisateurService.connexion(email, password);
            String token = jwtUtil.generateToken(utilisateur);

            //ConnexionResponse response = new ConnexionResponse(utilisateur);
            ConnexionResponse response = new ConnexionResponse(token, utilisateur);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la connexion");
        }
    }

    private Map<String, String> validerDonneesUtilisateur(Utilisateur utilisateur) {
        Map<String, String> erreurs = new HashMap<>();

        // Validation de l'email
        if (!validerEmail(utilisateur.getEmail())) {
            erreurs.put("email", "Format d'email invalide");
        }

        // Validation du pseudo
        if (utilisateur.getPseudo() == null || utilisateur.getPseudo().length() < 3) {
            erreurs.put("pseudo", "Le pseudo doit contenir au moins 3 caractères");
        }

        // Validation du code postal
        if (!validerCodePostal(utilisateur.getCodepostal())) {
            erreurs.put("codePostal", "Code postal invalide");
        }

        // Validation de la ville
        if (utilisateur.getVille() == null || utilisateur.getVille().trim().isEmpty()) {
            erreurs.put("ville", "La ville est requise");
        }

        // Validation du mot de passe
        if (!validerMotDePasse(utilisateur.getPassword())) {
            erreurs.put("motDePasse", "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre");
        }

        // Validation de l'année de naissance
        if (utilisateur.getAnneenaissance() == null ||
                utilisateur.getAnneenaissance() < 1900 ||
                utilisateur.getAnneenaissance() > java.time.Year.now().getValue()) {
            erreurs.put("anneeNaissance", "Année de naissance invalide");
        }

        // Validation de l'état civil
        if (utilisateur.getEtatcivil() == null || utilisateur.getEtatcivil().trim().isEmpty()) {
            erreurs.put("etatCivil", "L'état civil est requis");
        }

        return erreurs;
    }

    private boolean validerEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private boolean validerCodePostal(String codePostal) {
        return codePostal != null && codePostal.matches("^[0-9]{5}$");
    }

    private boolean validerMotDePasse(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean contientMajuscule = false;
        boolean contientMinuscule = false;
        boolean contientChiffre = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) contientMajuscule = true;
            if (Character.isLowerCase(c)) contientMinuscule = true;
            if (Character.isDigit(c)) contientChiffre = true;
        }

        return contientMajuscule && contientMinuscule && contientChiffre;
    }
}
