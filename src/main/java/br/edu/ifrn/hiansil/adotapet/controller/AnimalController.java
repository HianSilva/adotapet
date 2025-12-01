package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AnimalRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AnimalResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/animais")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    public ResponseEntity<AnimalResponseDTO> cadastrar(@RequestBody @Valid AnimalRequestDTO dto, UriComponentsBuilder uriBuilder) {
        AnimalResponseDTO animalSalvo = animalService.cadastrar(dto);

        URI uri = uriBuilder.path("/api/v1/animais/{id}").buildAndExpand(animalSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(animalSalvo);
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> listarDisponiveis() {
        return ResponseEntity.ok(animalService.listarDisponiveis());
    }
}