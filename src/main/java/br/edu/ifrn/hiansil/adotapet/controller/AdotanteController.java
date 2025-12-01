package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AdotanteRequestDTO;
import br.edu.ifrn.hiansil.adotapet.service.AdotanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/adotantes")
@RequiredArgsConstructor
public class AdotanteController {

    private final AdotanteService adotanteService;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid AdotanteRequestDTO dto) {
        adotanteService.cadastrar(dto);
        return ResponseEntity.status(201).body("Adotante cadastrado com sucesso!");
    }
}