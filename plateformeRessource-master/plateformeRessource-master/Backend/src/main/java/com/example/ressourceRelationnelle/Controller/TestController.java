package com.example.ressourceRelationnelle.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    public TestController() {
        System.out.println("TestController instancié");  // Log de vérification
    }

    @GetMapping
    public String test() {
        return "OK";
    }
}
