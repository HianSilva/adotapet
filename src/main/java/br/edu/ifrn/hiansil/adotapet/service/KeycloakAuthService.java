package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.request.LoginRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.TokenResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.ws.rs.core.Response;
import java.util.Collections;

/**
 * Serviço para gerenciamento de autenticação de ABRIGOs via Keycloak.
 * Responsável por registro de abrigos no Keycloak e obtenção de tokens.
 */
@Service
@Slf4j
public class KeycloakAuthService {

    private final Keycloak keycloak;
    private final String realm;
    private final String clientId;
    private final String clientSecret;
    private final String authServerUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KeycloakAuthService(
            Keycloak keycloak,
            @Value("${keycloak.realm}") String realm,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret,
            @Value("${keycloak.auth-server-url}") String authServerUrl
    ) {
        this.keycloak = keycloak;
        this.realm = realm;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authServerUrl = authServerUrl;
    }

    /**
     * Registra um novo ABRIGO no Keycloak.
     * O abrigo é criado com a role ABRIGO automaticamente.
     */
    public void registrarAbrigo(AbrigoRequestDTO dto) {
        try {
            // Criar representação do usuário
            UserRepresentation user = new UserRepresentation();
            user.setUsername(dto.getEmail());
            user.setEmail(dto.getEmail());
            
            // Dividir nome em firstName e lastName (requerido pelo Keycloak VERIFY_PROFILE)
            String[] nomes = dto.getNome().split(" ", 2);
            user.setFirstName(nomes[0]);
            user.setLastName(nomes.length > 1 ? nomes[1] : nomes[0]); // Se só tem um nome, repete
            
            user.setEnabled(true);
            user.setEmailVerified(true);
            user.setRequiredActions(Collections.emptyList()); // Sem ações requeridas

            // Configurar senha
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(dto.getSenha());
            credential.setTemporary(false);
            user.setCredentials(Collections.singletonList(credential));

            // Criar usuário no Keycloak
            Response response = keycloak.realm(realm).users().create(user);

            if (response.getStatus() == 201) {
                // Obter ID do usuário criado
                String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                
                // Remover required actions e configurar senha
                clearRequiredActionsAndSetPassword(userId, dto.getSenha());
                
                // Atribuir role ABRIGO
                assignAbrigoRole(userId);
                
                log.info("Abrigo criado com sucesso no Keycloak: {}", dto.getEmail());
            } else if (response.getStatus() == 409) {
                log.warn("Usuário já existe no Keycloak: {}", dto.getEmail());
                throw new IllegalArgumentException("Já existe um usuário cadastrado com este email no sistema de autenticação");
            } else {
                String errorMessage = response.readEntity(String.class);
                log.error("Erro ao criar usuário no Keycloak. Status: {}, Error: {}", 
                         response.getStatus(), errorMessage);
                throw new RuntimeException("Erro ao criar usuário no sistema de autenticação");
            }
            
            response.close();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao registrar abrigo no Keycloak", e);
            throw new RuntimeException("Erro ao registrar abrigo: " + e.getMessage());
        }
    }

    /**
     * Atribui a role ABRIGO ao usuário no Keycloak.
     */
    private void assignAbrigoRole(String userId) {
        try {
            var roleRepresentation = keycloak.realm(realm)
                    .roles()
                    .get("ABRIGO")
                    .toRepresentation();

            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(roleRepresentation));
            
            log.info("Role ABRIGO atribuída ao usuário {}", userId);
        } catch (Exception e) {
            log.error("Erro ao atribuir role ABRIGO ao usuário", e);
            throw new RuntimeException("Erro ao atribuir permissões ao usuário: " + e.getMessage());
        }
    }

    /**
     * Reseta a senha do usuário de forma que não requeira mudança posterior.
     */
    private void resetPassword(String userId, String password) {
        try {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .resetPassword(credential);
            
            log.info("Senha configurada para o usuário {}", userId);
        } catch (Exception e) {
            log.error("Erro ao configurar senha do usuário", e);
            throw new RuntimeException("Erro ao configurar senha: " + e.getMessage());
        }
    }

    /**
     * Remove todas as required actions do usuário e configura a senha.
     */
    private void clearRequiredActionsAndSetPassword(String userId, String password) {
        try {
            // Obter usuário atual
            UserRepresentation userRep = keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .toRepresentation();
            
            // Limpar required actions
            userRep.setRequiredActions(Collections.emptyList());
            userRep.setEmailVerified(true);
            userRep.setEnabled(true);
            
            // Atualizar usuário
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .update(userRep);
            
            // Configurar senha
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            
            keycloak.realm(realm)
                    .users()
                    .get(userId)
                    .resetPassword(credential);
            
            log.info("Required actions removidas e senha configurada para o usuário {}", userId);
        } catch (Exception e) {
            log.error("Erro ao configurar usuário", e);
            throw new RuntimeException("Erro ao configurar usuário: " + e.getMessage());
        }
    }

    /**
     * Realiza login do abrigo e obtém token do Keycloak.
     */
    public TokenResponseDTO login(LoginRequestDTO dto) {
        try {
            String tokenUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "password");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("username", dto.getEmail());
            body.add("password", dto.getSenha());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                
                TokenResponseDTO tokenResponse = new TokenResponseDTO();
                tokenResponse.setAccessToken(jsonNode.get("access_token").asText());
                tokenResponse.setRefreshToken(jsonNode.get("refresh_token").asText());
                tokenResponse.setTokenType(jsonNode.get("token_type").asText());
                tokenResponse.setExpiresIn(jsonNode.get("expires_in").asLong());
                
                return tokenResponse;
            }

            throw new RuntimeException("Erro ao obter token do Keycloak");

        } catch (HttpClientErrorException.Unauthorized e) {
            log.warn("Tentativa de login com credenciais inválidas: {}", dto.getEmail());
            throw new IllegalArgumentException("Email ou senha inválidos");
        } catch (HttpClientErrorException.BadRequest e) {
            log.warn("Requisição inválida ao fazer login: {}", dto.getEmail());
            throw new IllegalArgumentException("Email ou senha inválidos");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao fazer login", e);
            throw new RuntimeException("Erro ao fazer login: " + e.getMessage());
        }
    }

    /**
     * Remove um usuário do Keycloak pelo email.
     */
    public void removerUsuario(String email) {
        try {
            var users = keycloak.realm(realm).users().search(email);
            if (!users.isEmpty()) {
                String userId = users.get(0).getId();
                keycloak.realm(realm).users().delete(userId);
                log.info("Usuário removido do Keycloak: {}", email);
            }
        } catch (Exception e) {
            log.error("Erro ao remover usuário do Keycloak: {}", email, e);
        }
    }
}
