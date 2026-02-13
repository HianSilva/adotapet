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

    @Bean
    public OpenAPI customOpenAPI() {
        final String bearerSchemeName = "bearer-jwt";
        final String oauth2SchemeName = "oauth2";
        
        return new OpenAPI()
                .info(apiInfo())
                .externalDocs(new ExternalDocumentation()
                        .description("Keycloak Admin Console")
                        .url("http://localhost:8180/admin"))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento")
                ))
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
                .title("🐾 AdotaPet API")
                .version("1.0.0")
                .description("""
                    ## API para Gerenciamento de Adoção de Animais
                    
                    Esta API permite o gerenciamento completo do processo de adoção de animais,
                    incluindo cadastro de abrigos, animais e solicitações de adoção.
                    
                    ### 🔐 Autenticação
                    
                    A API utiliza **OAuth2/OpenID Connect** com **Keycloak** como Identity Provider.
                    
                    **Endpoints de autenticação (públicos):**
                    - `POST /api/v1/auth/registrar` - Registrar novo abrigo
                    - `POST /api/v1/auth/login` - Fazer login e obter token JWT
                    
                    **Opções de autenticação:**
                    1. **Via API**: Use os endpoints `/api/v1/auth/login` para obter token
                    2. **Bearer Token**: Cole o token no header `Authorization: Bearer <token>`
                    3. **OAuth2 Flow**: Use o fluxo OAuth2 diretamente pelo Swagger UI
                    
                    ### 👥 Role Disponível
                    
                    | Role | Descrição | Permissões |
                    |------|-----------|------------|
                    | `ABRIGO` | Abrigo de animais | Gerenciar animais e ver solicitações |
                    
                    ### 🚀 Quick Start
                    
                    1. **Registrar um abrigo:**
                       ```json
                       POST /api/v1/auth/registrar
                       {
                         "nome": "Meu Abrigo",
                         "email": "meu@abrigo.com",
                         "documento": "12345678901234",
                         "telefone": "84999999999",
                         "senha": "Senha@123"
                       }
                       ```
                    
                    2. **Fazer login:**
                       ```json
                       POST /api/v1/auth/login
                       {
                         "email": "meu@abrigo.com",
                         "senha": "Senha@123"
                       }
                       ```
                    
                    3. Copie o `accessToken` da resposta
                    4. Clique em "Authorize" e cole o token
                    5. Teste os endpoints protegidos!
                    
                    ### 📚 Links Úteis
                    
                    - [Keycloak Admin](http://localhost:8180/admin) (admin/admin)
                    - [Keycloak Account](http://localhost:8180/realms/adotapet/account)
                    """)
                .contact(new Contact()
                        .name("IFRN - Instituto Federal do Rio Grande do Norte")
                        .email("contato@ifrn.edu.br")
                        .url("https://portal.ifrn.edu.br"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));
    }
}
