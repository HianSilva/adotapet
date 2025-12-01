package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.AbrigoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/abrigos")
@RequiredArgsConstructor
public class AbrigoController {

    private final AbrigoService abrigoService;

    @PostMapping
    public ResponseEntity<AbrigoResponseDTO> cadastrar(@RequestBody @Valid AbrigoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        AbrigoResponseDTO abrigoCadastrado = abrigoService.cadastrar(dto);

        // Cria a URI para o cabeçalho 'Location' (Boas práticas REST)
        URI uri = uriBuilder.path("/api/v1/abrigos/{id}").buildAndExpand(abrigoCadastrado.getId()).toUri();

        return ResponseEntity.created(uri).body(abrigoCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<AbrigoResponseDTO>> listar() {
        return ResponseEntity.ok(abrigoService.listar());
    }
}