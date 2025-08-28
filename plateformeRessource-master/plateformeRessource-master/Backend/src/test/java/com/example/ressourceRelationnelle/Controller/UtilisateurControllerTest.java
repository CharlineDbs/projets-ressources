package com.example.ressourceRelationnelle.Controller;

import com.example.ressourceRelationnelle.DTO.ConnexionRequest;
import com.example.ressourceRelationnelle.DTO.ConnexionResponse;
import com.example.ressourceRelationnelle.DTO.JwtUtil;
import com.example.ressourceRelationnelle.Modele.Utilisateur;
import com.example.ressourceRelationnelle.Service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UtilisateurControllerTest {

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UtilisateurController utilisateurController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inscription_Success() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@test.com");
        utilisateur.setPseudo("testUser");
        utilisateur.setPassword("Password123");
        utilisateur.setCodepostal("75000");
        utilisateur.setVille("Paris");
        utilisateur.setAnneenaissance(1990);
        utilisateur.setEtatcivil("H");

        when(utilisateurService.inscription(any(Utilisateur.class))).thenReturn(utilisateur);

        // Act
        ResponseEntity<?> response = utilisateurController.inscription(utilisateur);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void inscription_EmailInvalide() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("invalid-email");
        utilisateur.setPseudo("testUser");
        utilisateur.setPassword("Password123");

        // Act
        ResponseEntity<?> response = utilisateurController.inscription(utilisateur);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void connexion_Success() {
        // Arrange
        ConnexionRequest request = new ConnexionRequest("test@test.com", "Password123");
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@test.com");
        utilisateur.setPseudo("testUser");

        when(utilisateurService.connexion(anyString(), anyString())).thenReturn(utilisateur);
        when(jwtUtil.generateToken(any(Utilisateur.class))).thenReturn("test-token");

        // Act
        ResponseEntity<?> response = utilisateurController.connexion(request);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof ConnexionResponse);
        ConnexionResponse connexionResponse = (ConnexionResponse) response.getBody();
        assertEquals("test-token", connexionResponse.getToken());
    }

    @Test
    void connexion_EmailManquant() {
        // Arrange
        ConnexionRequest request = new ConnexionRequest();
        request.setPassword("Password123");

        // Act
        ResponseEntity<?> response = utilisateurController.connexion(request);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Email et mot de passe requis", response.getBody());
    }

    @Test
    void connexion_EmailInvalide() {
        // Arrange
        ConnexionRequest request = new ConnexionRequest("invalid-email", "Password123");

        // Act
        ResponseEntity<?> response = utilisateurController.connexion(request);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Format d'email invalide", response.getBody());
    }

    @Test
    void connexion_ErreurAuthentification() {
        // Arrange
        ConnexionRequest request = new ConnexionRequest("test@test.com", "WrongPassword");
        when(utilisateurService.connexion(anyString(), anyString()))
            .thenThrow(new RuntimeException("Mot de passe incorrect"));

        // Act
        ResponseEntity<?> response = utilisateurController.connexion(request);

        // Assert
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Mot de passe incorrect", response.getBody());
    }

    @Test
    void inscription_ValidationMotDePasse() {
        // Arrange
        Object[][] casDeTest = {
                {"Pass1", "mot de passe trop court"},
                {"password123", "pas de majuscule"},
                {"PASSWORD123", "pas de minuscule"},
                {"Password", "pas de chiffre"},
                {"Password123", "mot de passe valide"}
        };

        for (Object[] cas : casDeTest) {
            String motDePasse = (String) cas[0];
            String description = (String) cas[1];

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail("test@test.com");
            utilisateur.setPseudo("testUser");
            utilisateur.setPassword(motDePasse);
            utilisateur.setCodepostal("75000");
            utilisateur.setVille("Paris");
            utilisateur.setAnneenaissance(1990);
            utilisateur.setEtatcivil("F");

            // Act
            ResponseEntity<?> response = utilisateurController.inscription(utilisateur);

            // Assert
            assertNotNull(response, "La réponse ne devrait pas être null pour le cas: " + description);

            if (motDePasse.equals("Password123")) {
                // Cas du mot de passe valide
                assertEquals(200, response.getStatusCodeValue(),
                        "Le statut devrait être 200 pour un mot de passe valide");
                when(utilisateurService.inscription(any(Utilisateur.class))).thenReturn(utilisateur);
            } else {
                // Cas des mots de passe invalides
                assertEquals(400, response.getStatusCodeValue(),
                        "Le statut devrait être 400 pour un mot de passe invalide: " + description);
                Map<String, String> erreurs = (Map<String, String>) response.getBody();
                assertNotNull(erreurs, "Les erreurs ne devraient pas être null pour: " + description);
                assertEquals("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre",
                        erreurs.get("motDePasse"),
                        "Message d'erreur incorrect pour: " + description);
            }
        }
    }


} 