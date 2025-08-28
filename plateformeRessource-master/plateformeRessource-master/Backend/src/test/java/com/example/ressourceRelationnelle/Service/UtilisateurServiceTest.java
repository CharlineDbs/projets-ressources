package com.example.ressourceRelationnelle.Service;

import com.example.ressourceRelationnelle.Service.UtilisateurService;
import com.example.ressourceRelationnelle.Modele.Utilisateur;
import com.example.ressourceRelationnelle.DataAccess.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UtilisateurService utilisateurService;

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

        when(utilisateurRepository.findByEmail(anyString())).thenReturn(null);
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);

        // Act
        Utilisateur result = utilisateurService.inscription(utilisateur);

        // Assert
        assertNotNull(result);
        assertTrue(result.getActif());
        verify(utilisateurRepository).save(any(Utilisateur.class));
    }

    @Test
    void inscription_EmailDejaUtilise() {
        // Arrange
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail("test@test.com");
        
        when(utilisateurRepository.findByEmail(anyString())).thenReturn(new Utilisateur());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> utilisateurService.inscription(utilisateur));
    }

    @Test
    void connexion_Success() {
        // Arrange
        String email = "test@test.com";
        String password = "Password123";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setPassword(utilisateurService.hashMD5(password));
        utilisateur.setActif(true);

        when(utilisateurRepository.findByEmail(email)).thenReturn(utilisateur);

        // Act
        Utilisateur result = utilisateurService.connexion(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void connexion_EmailInexistant() {
        // Arrange
        String email = "nonexistent@test.com";
        String password = "Password123";

        when(utilisateurRepository.findByEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> utilisateurService.connexion(email, password));
    }

    @Test
    void connexion_MotDePasseIncorrect() {
        // Arrange
        String email = "test@test.com";
        String password = "WrongPassword";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setPassword(utilisateurService.hashMD5("CorrectPassword"));
        utilisateur.setActif(true);

        when(utilisateurRepository.findByEmail(email)).thenReturn(utilisateur);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> utilisateurService.connexion(email, password));
    }

    @Test
    void connexion_CompteDesactive() {
        // Arrange
        String email = "test@test.com";
        String password = "Password123";
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(email);
        utilisateur.setPassword(utilisateurService.hashMD5(password));
        utilisateur.setActif(false);

        when(utilisateurRepository.findByEmail(email)).thenReturn(utilisateur);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> utilisateurService.connexion(email, password));
    }
} 