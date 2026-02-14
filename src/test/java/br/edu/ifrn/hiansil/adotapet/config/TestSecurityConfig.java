package br.edu.ifrn.hiansil.adotapet.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
@Profile("test")
public class TestSecurityConfig {

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/api/v1/abrigos",
            "/api/v1/animais/disponiveis",
            "/api/v1/animais/todos",
            "/api/v1/animais/abrigo/**",
            "/api/v1/animais/raca/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/h2-console/**"
    };

    @Bean
    @Primary
    @Order(1)
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .requestMatchers("/api/v1/abrigos/**").hasRole("ABRIGO")
                        .requestMatchers("/api/v1/animais/**").hasRole("ABRIGO")
                        .requestMatchers("/api/v1/solicitacoes-adocao/**").hasRole("ABRIGO")
                        .requestMatchers("/api/v1/adotantes/**").hasRole("ABRIGO")
                        .anyRequest().authenticated()
                )
                .build();
    }
}
