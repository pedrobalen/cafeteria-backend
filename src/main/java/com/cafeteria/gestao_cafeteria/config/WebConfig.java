package com.cafeteria.gestao_cafeteria.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Aplica o CORS para todos os endpoints sob /api/
                        .allowedOrigins("*")   // Permite requisições de qualquer origem (ótimo para dev)
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE") // Métodos HTTP permitidos
                        .allowedHeaders("*");  // Permite todos os cabeçalhos
            }
        };
    }
}