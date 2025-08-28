package com.example.ressourceRelationnelle.Service;

import com.example.ressourceRelationnelle.DataAccess.UtilisateurRepository;
import com.example.ressourceRelationnelle.Modele.Utilisateur;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Utilisateur> getAllUtilisateur() {
        return utilisateurRepository.findAll();
    }

    @Transactional
    public Utilisateur inscription(Utilisateur utilisateur) {
        try {
            // Vérifier si l'email existe déjà
            if (utilisateurRepository.findByEmail(utilisateur.getEmail()) != null) {
                throw new RuntimeException("Cet email est déjà utilisé");
            }

            // Vérifier que tous les champs requis sont présents
            if (utilisateur.getEmail() == null || utilisateur.getPseudo() == null ||
                    utilisateur.getCodepostal() == null || utilisateur.getVille() == null ||
                    utilisateur.getPassword() == null || utilisateur.getAnneenaissance() == null ||
                    utilisateur.getEtatcivil() == null) {
                throw new RuntimeException("Tous les champs sont obligatoires");
            }

            // Hacher le mot de passe en MD5
            String motDePasseHache = hashMD5(utilisateur.getPassword());
            utilisateur.setPassword(motDePasseHache);
            utilisateur.setActif(true);

            // Sauvegarder l'utilisateur en utilisant le repository
            return utilisateurRepository.save(utilisateur);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'utilisateur: " + e.getMessage());
        }
    }

    String hashMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    public Utilisateur connexion(String email, String password) {
        try {
            // Vérifier si l'email existe
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email);
            if (utilisateur == null) {
                throw new RuntimeException("Aucun compte associé à cet email");
            }

            // Vérifier si le compte est actif
            if (utilisateur.getActif() == null || !utilisateur.getActif()) {
                throw new RuntimeException("Ce compte a été désactivé");
            }

            // Vérifier le mot de passe
            String hashedPassword = hashMD5(password);
            if (!hashedPassword.equals(utilisateur.getPassword())) {
                throw new RuntimeException("Mot de passe incorrect");
            }

            return utilisateur;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la connexion: " + e.getMessage());
        }
    }
}
