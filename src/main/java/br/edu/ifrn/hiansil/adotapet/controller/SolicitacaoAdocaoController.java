package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.SolicitacaoAdocaoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.SolicitacaoAdocaoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.SolicitacaoAdocaoService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitacoes")
@RequiredArgsConstructor
public class SolicitacaoAdocaoController {

    private final SolicitacaoAdocaoService solicitacaoService;

    @PostMapping
    public ResponseEntity<String> solicitar(@RequestBody @Valid SolicitacaoAdocaoRequestDTO dto) {
        solicitacaoService.solicitar(dto);
        return ResponseEntity.status(201).body("Solicitação enviada com sucesso! Aguarde a análise do abrigo.");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> atualizarStatus(@PathVariable Long id, @RequestBody AtualizacaoStatusDTO statusDto) {
        solicitacaoService.atualizarStatus(id, statusDto.getStatus());
        return ResponseEntity.ok("Status da solicitação atualizado para: " + statusDto.getStatus());
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listar() {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listar();
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/adotante/{adotanteId}")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorAdotante(@PathVariable Long adotanteId) {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listarPorAdotante(adotanteId);
        return ResponseEntity.ok(solicitacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoAdocaoResponseDTO> obterPorId(@PathVariable Long id) {
        SolicitacaoAdocaoResponseDTO solicitacao = solicitacaoService.obterPorId(id);
        return ResponseEntity.ok(solicitacao);
    }

    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<SolicitacaoAdocaoResponseDTO>> listarPorAnimal(@PathVariable Long animalId) {
        List<SolicitacaoAdocaoResponseDTO> solicitacoes = solicitacaoService.listarPorAnimal(animalId);
        return ResponseEntity.ok(solicitacoes);
    }

    @Data
    public static class AtualizacaoStatusDTO {
        private String status;
    }
}