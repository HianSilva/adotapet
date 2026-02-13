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
@Tag(name = "Adotantes", description = "Gerenciamento de adotantes. Requer role ADOTANTE ou ADMIN.")
@SecurityRequirement(name = "bearer-jwt")
public class AdotanteController {

    private final AdotanteService adotanteService;

    @PostMapping
    @Operation(summary = "Cadastrar novo adotante", description = "Cria um novo adotante no sistema. Requer role ADOTANTE ou ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AdotanteRequestDTO dto) {
        adotanteService.cadastrar(dto);
        return ResponseEntity.status(201).body("Adotante cadastrado com sucesso!");
    }

    @GetMapping
    @Operation(summary = "Listar todos os adotantes", description = "Lista todos os adotantes cadastrados. Requer role ADOTANTE ou ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<List<AdotanteResponseDTO>> listar() {
        List<AdotanteResponseDTO> adotantes = adotanteService.listar();
        return ResponseEntity.ok(adotantes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter adotante por ID", description = "Busca um adotante específico pelo ID. Requer role ADOTANTE ou ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<AdotanteResponseDTO> obterPorId(@PathVariable Long id) {
        AdotanteResponseDTO adotante = adotanteService.obterPorId(id);
        return ResponseEntity.ok(adotante);
    }

    @PostMapping("/{id}/bloquear")
    @Operation(summary = "Bloquear adotante", description = "Bloqueia um adotante, impedindo novas solicitações. Requer role ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<String> bloquearAdotante(@PathVariable Long id) {
        adotanteService.bloquearAdotante(id);
        return ResponseEntity.ok("Adotante bloqueado com sucesso.");
    }

    @PostMapping("/{id}/desbloquear")
    @Operation(summary = "Desbloquear adotante", description = "Remove o bloqueio de um adotante. Requer role ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<String> desbloquearAdotante(@PathVariable Long id) {
        adotanteService.desbloquearAdotante(id);
        return ResponseEntity.ok("Adotante desbloqueado com sucesso.");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar adotante", description = "Remove um adotante do sistema. Requer role ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<String> deletarAdotante(@PathVariable Long id) {
        adotanteService.deletarAdotante(id);
        return ResponseEntity.noContent().build();
    }
}