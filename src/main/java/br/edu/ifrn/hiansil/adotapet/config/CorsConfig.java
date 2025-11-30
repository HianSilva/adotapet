package br.edu.ifrn.hiansil.adotapet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a configuração para TODAS as rotas da API
                .allowedOrigins("*") // Permite requisições de QUALQUER origem (Frontend, Postman, Mobile)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT") // Métodos permitidos
                .allowedHeaders("*"); // Permite todos os cabeçalhos (Authorization, Content-Type, etc.)
    }
}