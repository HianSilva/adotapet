package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.SolicitacaoAdocaoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.service.SolicitacaoAdocaoService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Data
    public static class AtualizacaoStatusDTO {
        private String status;
    }
}