package com.example.ressourceRelationnelle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class WebConfig {

    public WebConfig(RequestMappingHandlerMapping handlerMapping) {
        handlerMapping.getHandlerMethods().forEach((key, value) ->
            System.out.println("Mapped: " + key + " -> " + value)
        );
    }
}
