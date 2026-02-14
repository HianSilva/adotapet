package br.edu.ifrn.hiansil.adotapet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do Swagger/OpenAPI com suporte a OAuth2/Keycloak.
 * 
 * A documentação está disponível em:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri:http://localhost:8180/realms/adotapet}")
    private String keycloakIssuerUri;

    @Value("${app.server.url:http://localhost:8080}")
    private String serverUrl;

    @Value("${app.server.production-url:}")
    private String productionUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        final String bearerSchemeName = "bearer-jwt";
        final String oauth2SchemeName = "oauth2";

        List<Server> servers = new java.util.ArrayList<>();
        servers.add(new Server().url(serverUrl).description("Servidor Local"));
        
        if (productionUrl != null && !productionUrl.isEmpty()) {
            servers.add(new Server().url(productionUrl).description("Servidor de Produção (AWS)"));
        }
        
        return new OpenAPI()
                .info(apiInfo())
                .externalDocs(new ExternalDocumentation()
                        .description("Keycloak Admin Console")
                        .url(keycloakIssuerUri.replace("/realms/adotapet", "/admin")))
                .servers(servers)
                .components(new Components()
                        // Esquema Bearer JWT (para uso direto com token)
                        .addSecuritySchemes(bearerSchemeName, new SecurityScheme()
                                .name(bearerSchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                    **Autenticação via JWT Token**
                                    
                                    Obtenha o token via Keycloak e cole aqui.
                                    
                                    **Exemplo com curl:**
                                    ```bash
                                    curl -X POST "http://localhost:8180/realms/adotapet/protocol/openid-connect/token" \\
                                      -H "Content-Type: application/x-www-form-urlencoded" \\
                                      -d "client_id=adotapet-api" \\
                                      -d "client_secret=dev-secret-change-in-prod" \\
                                      -d "grant_type=password" \\
                                      -d "username=abrigo@teste.com" \\
                                      -d "password=abrigo123"
                                    ```
                                    
                                    **Ou use o endpoint da API:**
                                    ```bash
                                    curl -X POST "http://localhost:8080/api/v1/auth/login" \\
                                      -H "Content-Type: application/json" \\
                                      -d '{"email": "abrigo@teste.com", "senha": "abrigo123"}'
                                    ```
                                    
                                    **Usuário de teste:**
                                    - abrigo@teste.com / abrigo123 (ABRIGO)
                                    """))
                        // Esquema OAuth2 (para autenticação via fluxo OAuth2)
                        .addSecuritySchemes(oauth2SchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .description("Autenticação OAuth2 via Keycloak")
                                .flows(new OAuthFlows()
                                        // Password Flow (Resource Owner Password Credentials)
                                        .password(new OAuthFlow()
                                                .tokenUrl(keycloakIssuerUri + "/protocol/openid-connect/token")
                                                .scopes(new Scopes()
                                                        .addString("openid", "OpenID Connect scope")
                                                        .addString("profile", "Acesso ao perfil do usuário")
                                                        .addString("email", "Acesso ao email do usuário")))
                                        // Authorization Code Flow (recomendado para SPAs)
                                        .authorizationCode(new OAuthFlow()
                                                .authorizationUrl(keycloakIssuerUri + "/protocol/openid-connect/auth")
                                                .tokenUrl(keycloakIssuerUri + "/protocol/openid-connect/token")
                                                .scopes(new Scopes()
                                                        .addString("openid", "OpenID Connect scope")
                                                        .addString("profile", "Acesso ao perfil do usuário")
                                                        .addString("email", "Acesso ao email do usuário")))
                                ))
                );
                // Segurança NÃO é aplicada globalmente - cada endpoint define sua própria
    }

    private Info apiInfo() {
        return new Info()
                .title("AdotaPet API")
                .version("1.0.0");
    }
}
