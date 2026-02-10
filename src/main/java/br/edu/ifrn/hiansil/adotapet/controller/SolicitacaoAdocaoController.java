package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.SolicitacaoAdocaoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.SolicitacaoAdocaoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.SolicitacaoAdocaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitacoes")
@RequiredArgsConstructor
@Tag(name = "Solicitações de Adoção", description = "Gerenciamento de solicitações de adoção - Rotas protegidas")
@SecurityRequirement(name = "bearerAuth")
public class SolicitacaoAdocaoController {

    private final SolicitacaoAdocaoService solicitacaoService;

    @PostMapping
    @Operation(summary = "Solicitar adoção", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoAdocaoRequestDTO dto) {
        solicitacaoService.solicitar(dto);
        return ResponseEntity.status(201).body("Solicitação enviada com sucesso! Aguarde a análise do abrigo.");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status da solicitação", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> atualizarStatus(@PathVariable Long id, @RequestBody AtualizacaoStatusDTO statusDto) {
        solicitacaoService.atualizarStatus(id, statusDto.getStatus());
        return ResponseEntity.ok("Status da solicitação atualizado para: " + statusDto.getStatus());
    }

    @GetMapping
    @Operation(summary = "Listar todas as solicitações", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listar() {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listar();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/adotante/{adotanteId}")
    @Operation(summary = "Listar solicitações por adotante", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorAdotante(@PathVariable Long adotanteId) {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listarPorAdotante(adotanteId);
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter solicitação por ID", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<SolicitacaoAdocaoResponseDTO> obterPorId(@PathVariable Long id) {
        SolicitacaoAdocaoResponseDTO solicitacao = solicitacaoService.obterPorId(id);
        return ResponseEntity.ok(solicitacao);
    }

    @GetMapping("/animal/{animalId}")
    @Operation(summary = "Listar solicitações por animal", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorAnimal(@PathVariable Long animalId) {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listarPorAnimal(animalId);
        return ResponseEntity.ok(solicitacoes);
    }

    @Data
    public static class AtualizacaoStatusDTO {
        private String status;
    }
}