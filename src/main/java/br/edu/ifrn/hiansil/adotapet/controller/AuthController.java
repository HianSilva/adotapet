package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.request.LoginRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.TokenResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.AbrigoService;
import br.edu.ifrn.hiansil.adotapet.service.KeycloakAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller responsável por autenticação e registro de ABRIGOs.
 * Integra com Keycloak para gerenciamento de identidade.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticação", description = "Endpoints para registro e login de abrigos")
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;
    private final AbrigoService abrigoService;

    @PostMapping("/registrar")
    @Operation(
        summary = "Registrar novo abrigo",
        description = "Cria um novo abrigo no sistema e no Keycloak. O abrigo receberá automaticamente a role ABRIGO.",
        security = {}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Abrigo registrado com sucesso",
            content = @Content(schema = @Schema(implementation = AbrigoResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos ou abrigo já existe",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Erro interno ao registrar abrigo",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<?> registrar(@Valid @RequestBody AbrigoRequestDTO dto) {
        try {
            // 1. Registrar no Keycloak primeiro
            keycloakAuthService.registrarAbrigo(dto);
            
            // 2. Cadastrar no banco de dados local
            AbrigoResponseDTO abrigoResponse = abrigoService.cadastrar(dto);
            
            log.info("Abrigo registrado com sucesso: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(abrigoResponse);
            
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao registrar abrigo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao registrar abrigo", e);
            // Se falhou após criar no Keycloak, tenta remover
            try {
                keycloakAuthService.removerUsuario(dto.getEmail());
            } catch (Exception ex) {
                log.error("Erro ao fazer rollback do usuário no Keycloak", ex);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao registrar abrigo: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    @Operation(
        summary = "Fazer login",
        description = "Autentica o abrigo e retorna tokens de acesso JWT",
        security = {}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso",
            content = @Content(schema = @Schema(implementation = TokenResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciais inválidas",
            content = @Content(schema = @Schema(implementation = Map.class))
        )
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        try {
            TokenResponseDTO token = keycloakAuthService.login(dto);
            log.info("Login realizado com sucesso: {}", dto.getEmail());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            log.warn("Falha no login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            log.error("Erro ao fazer login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao fazer login"));
        }
    }
}
