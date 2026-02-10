package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AdotanteRequestDTO;
import br.edu.ifrn.hiansil.adotapet.service.AdotanteService;
import br.edu.ifrn.hiansil.adotapet.dto.response.AdotanteResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/v1/adotantes")
@RequiredArgsConstructor
@Tag(name = "Adotantes", description = "Gerenciamento de adotantes - Rotas protegidas")
@SecurityRequirement(name = "bearerAuth")
public class AdotanteController {

    private final AdotanteService adotanteService;

    @PostMapping
    @Operation(summary = "Cadastrar novo adotante", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AdotanteRequestDTO dto) {
        adotanteService.cadastrar(dto);
        return ResponseEntity.status(201).body("Adotante cadastrado com sucesso!");
    }

    @GetMapping
    @Operation(summary = "Listar todos os adotantes", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<AdotanteResponseDTO>> listar() {
        List<AdotanteResponseDTO> adotantes = adotanteService.listar();
        return ResponseEntity.ok(adotantes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter adotante por ID", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AdotanteResponseDTO> obterPorId(@PathVariable Long id) {
        AdotanteResponseDTO adotante = adotanteService.obterPorId(id);
        return ResponseEntity.ok(adotante);
    }

    @PostMapping("/{id}/bloquear")
    @Operation(summary = "Bloquear adotante", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> bloquearAdotante(@PathVariable Long id) {
        adotanteService.bloquearAdotante(id);
        return ResponseEntity.ok("Adotante bloqueado com sucesso.");
    }

    @PostMapping("/{id}/desbloquear")
    @Operation(summary = "Desbloquear adotante", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> desbloquearAdotante(@PathVariable Long id) {
        adotanteService.desbloquearAdotante(id);
        return ResponseEntity.ok("Adotante desbloqueado com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarAdotante(@PathVariable Long id) {
        adotanteService.deletarAdotante(id);
        return ResponseEntity.noContent().build();
    }
}