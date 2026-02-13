package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.AbrigoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

/**
 * Controller para gerenciamento de Abrigos.
 * A autenticação é via OAuth2/JWT com Keycloak.
 */
@RestController
@RequestMapping("/api/v1/abrigos")
@RequiredArgsConstructor
@Tag(name = "Abrigos", description = "Gerenciamento de abrigos de animais. Requer role ABRIGO ou ADMIN para endpoints protegidos.")
public class AbrigoController {

    private final AbrigoService abrigoService;

    @PostMapping("/cadastrar")
    @Operation(
        summary = "Cadastrar novo abrigo",
        description = "Cria um novo abrigo no sistema. Requer autenticação com role ABRIGO ou ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Abrigo criado com sucesso",
            content = @Content(schema = @Schema(implementation = AbrigoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado - Token JWT ausente ou inválido"),
        @ApiResponse(responseCode = "403", description = "Acesso negado - Role insuficiente"),
        @ApiResponse(responseCode = "409", description = "Email ou documento já cadastrado")
    })
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<AbrigoResponseDTO> cadastrar(
            @RequestBody @Valid AbrigoRequestDTO dto,
            @AuthenticationPrincipal Jwt jwt,
            UriComponentsBuilder uriBuilder
    ) {
        AbrigoResponseDTO abrigoCadastrado = abrigoService.cadastrar(dto);
        URI uri = uriBuilder.path("/api/v1/abrigos/{id}").buildAndExpand(abrigoCadastrado.getId()).toUri();
        return ResponseEntity.created(uri).body(abrigoCadastrado);
    }

    @GetMapping
    @Operation(
        summary = "Listar todos os abrigos",
        description = "Endpoint **público** - não requer autenticação. Retorna lista de todos os abrigos cadastrados.",
        security = {}  // Endpoint público - sem segurança
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de abrigos retornada com sucesso")
    })
    public ResponseEntity<List<AbrigoResponseDTO>> listar() {
        return ResponseEntity.ok(abrigoService.listar());
    }

    @GetMapping("/me")
    @Operation(
        summary = "Obter dados do abrigo logado",
        description = "Retorna os dados do abrigo associado ao email contido no token JWT. Útil para obter perfil do usuário autenticado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados do abrigo retornados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Abrigo não encontrado para o email do token")
    })
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<AbrigoResponseDTO> getAbrigoLogado(
            @Parameter(hidden = true) @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        return ResponseEntity.ok(abrigoService.buscarPorEmail(email));
    }
}