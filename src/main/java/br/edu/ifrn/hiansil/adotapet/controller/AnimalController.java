package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AnimalRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AnimalResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Animais", description = "Gerenciamento de animais para adoção")
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping
    @Operation(summary = "Cadastrar novo animal", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AnimalResponseDTO> cadastrar(@RequestBody @Valid AnimalRequestDTO dto, UriComponentsBuilder uriBuilder) {
        AnimalResponseDTO animalSalvo = animalService.cadastrar(dto);

        URI uri = uriBuilder.path("/api/v1/animais/{id}").buildAndExpand(animalSalvo.getId()).toUri();

        return ResponseEntity.created(uri).body(animalSalvo);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Listar animais disponíveis", description = "Endpoint público para listar animais disponíveis para adoção")
    public ResponseEntity<List<AnimalResponseDTO>> listarDisponiveis() {
        return ResponseEntity.ok(animalService.listarDisponiveis());
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os animais", description = "Endpoint público para listar todos os animais")
    public ResponseEntity<List<AnimalResponseDTO>> listarTodos() {
        return ResponseEntity.ok(animalService.listarTodos());
    }   

    @GetMapping("/abrigo/{abrigoId}")
    @Operation(summary = "Listar animais por abrigo", description = "Endpoint público para listar animais de um abrigo específico")
    public ResponseEntity<List<AnimalResponseDTO>> listarPorAbrigo(@PathVariable Long abrigoId) {
        return ResponseEntity.ok(animalService.listarPorAbrigo(abrigoId));
    }

    @GetMapping("/raca/{racaId}")
    @Operation(summary = "Listar animais por raça", description = "Endpoint público para listar animais de uma raça específica")
    public ResponseEntity<List<AnimalResponseDTO>> listarPorRaca(@PathVariable Long racaId) {
        return ResponseEntity.ok(animalService.listarPorRaca(racaId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir animal", description = "Endpoint protegido - Requer autenticação JWT")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        animalService.deletarAnimal(id);
        return ResponseEntity.noContent().build();
    }
}