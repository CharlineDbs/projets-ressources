package com.example.ressourceRelationnelle.DTO;


import com.example.ressourceRelationnelle.Modele.Utilisateur;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.hmacShaKeyFor("votreSuperCleSecreteUltraLongueDe256bits".getBytes(StandardCharsets.UTF_8));

    public String generateToken(Utilisateur utilisateur) {
        return Jwts.builder()
            .setSubject(utilisateur.getEmail())
            .claim("id", utilisateur.getId())
            .claim("pseudo", utilisateur.getPseudo())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }
}
