package com.example.ressourceRelationnelle.Controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:8081") // <-- autorise ton frontend
@RestController
@RequestMapping("/api")
public class FileUploadController {

    private static final String BASE_UPLOAD_DIR = "C:\\Users\\nonom\\Documents\\Bachelor CDA\\Projet ressource\\Stockage_des_ressources"; // racine des fichiers

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/ressources/{id}/upload")
    // uploader un fichier lié à une ressource
    public ResponseEntity<String> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // Crée le dossier pour la ressource, ex: uploads/42
            File ressourceDir = new File(BASE_UPLOAD_DIR, String.valueOf(id));
            if (!ressourceDir.exists()) {
                ressourceDir.mkdirs();
            }

            // Chemin complet du fichier : uploads/42/nomoriginal.jpg
            String filePath = ressourceDir.getAbsolutePath() + File.separator + file.getOriginalFilename();
            File dest = new File(filePath);

            // Sauvegarde le fichier
            file.transferTo(dest);

            return ResponseEntity.ok("Fichier enregistré : " + dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de l'upload : " + e.getMessage());
        }
    }


    @GetMapping("/ressources/{id}/download")
    public ResponseEntity<byte[]> downloadSingleFile(@PathVariable Long id) {
        try {
            File ressourceDir = new File(BASE_UPLOAD_DIR + File.separator + id);

            if (!ressourceDir.exists() || !ressourceDir.isDirectory()) {
                return ResponseEntity.notFound().build();
            }

            File[] files = ressourceDir.listFiles();
            if (files == null || files.length == 0) {
                return ResponseEntity.notFound().build();
            }

            File file = files[0];  // On prend le premier (et normalement unique) fichier
            byte[] fileContent = StreamUtils.copyToByteArray(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/ressources/{id}/fileExist")
    public String fileExist(@PathVariable Long id){
        File ressourceDir = new File(BASE_UPLOAD_DIR + File.separator + id);

        if (!ressourceDir.exists() || !ressourceDir.isDirectory()) {
            return "";
        }

        File[] files = ressourceDir.listFiles();
        if (files == null || files.length == 0) {
            return "";
        }

        File file = files[0];
        return file.getName();// On prend le premier (et normalement unique) fichier
    }
}
