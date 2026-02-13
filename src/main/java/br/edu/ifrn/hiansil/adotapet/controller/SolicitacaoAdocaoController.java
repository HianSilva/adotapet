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
@Tag(name = "Solicitações de Adoção", description = "Gerenciamento de solicitações de adoção. Todos os endpoints requerem autenticação.")
@SecurityRequirement(name = "bearer-jwt")
public class SolicitacaoAdocaoController {

    private final SolicitacaoAdocaoService solicitacaoService;

    @PostMapping
    @Operation(summary = "Solicitar adoção", description = "Cria uma nova solicitação de adoção. Requer role ADOTANTE.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoAdocaoRequestDTO dto) {
        solicitacaoService.solicitar(dto);
        return ResponseEntity.status(201).body("Solicitação enviada com sucesso! Aguarde a análise do abrigo.");
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status da solicitação", description = "Aprova ou rejeita uma solicitação de adoção. Requer role ABRIGO ou ADMIN.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<String> atualizarStatus(@PathVariable Long id, @RequestBody AtualizacaoStatusDTO statusDto) {
        solicitacaoService.atualizarStatus(id, statusDto.getStatus());
        return ResponseEntity.ok("Status da solicitação atualizado para: " + statusDto.getStatus());
    }

    @GetMapping
    @Operation(summary = "Listar todas as solicitações", description = "Lista todas as solicitações de adoção. Requer autenticação.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listar() {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listar();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/adotante/{adotanteId}")
    @Operation(summary = "Listar solicitações por adotante", description = "Lista solicitações de um adotante específico. Requer autenticação.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorAdotante(@PathVariable Long adotanteId) {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listarPorAdotante(adotanteId);
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter solicitação por ID", description = "Busca uma solicitação específica pelo ID. Requer autenticação.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<SolicitacaoAdocaoResponseDTO> obterPorId(@PathVariable Long id) {
        SolicitacaoAdocaoResponseDTO solicitacao = solicitacaoService.obterPorId(id);
        return ResponseEntity.ok(solicitacao);
    }

    @GetMapping("/animal/{animalId}")
    @Operation(summary = "Listar solicitações por animal", description = "Lista solicitações para um animal específico. Requer autenticação.")
    @SecurityRequirement(name = "bearer-jwt")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorAnimal(@PathVariable Long animalId) {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listarPorAnimal(animalId);
        return ResponseEntity.ok(solicitacoes);
    }

    @Data
    public static class AtualizacaoStatusDTO {
        private String status;
    }
}