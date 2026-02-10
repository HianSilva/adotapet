package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.request.LoginRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.LoginResponseDTO;
import br.edu.ifrn.hiansil.adotapet.service.AbrigoService;
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
@RequestMapping("/api/v1/abrigos")
@RequiredArgsConstructor
@Tag(name = "Abrigos", description = "Gerenciamento de abrigos de animais")
public class AbrigoController {

    private final AbrigoService abrigoService;

    @PostMapping("/cadastrar")
    @Operation(summary = "Cadastrar novo abrigo", description = "Endpoint público para cadastro de novos abrigos")
    public ResponseEntity<AbrigoResponseDTO> cadastrar(@RequestBody @Valid AbrigoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        AbrigoResponseDTO abrigoCadastrado = abrigoService.cadastrar(dto);

        URI uri = uriBuilder.path("/api/v1/abrigos/{id}").buildAndExpand(abrigoCadastrado.getId()).toUri();

        return ResponseEntity.created(uri).body(abrigoCadastrado);
    }

    @PostMapping("/login")
    @Operation(summary = "Login de abrigo", description = "Endpoint público para autenticação e obtenção de token JWT")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        LoginResponseDTO loginResponse = abrigoService.login(dto);
        return ResponseEntity.ok(loginResponse);
    }


    @GetMapping
    @Operation(summary = "Listar todos os abrigos", description = "Endpoint público para listar todos os abrigos cadastrados")
    public ResponseEntity<List<AbrigoResponseDTO>> listar() {
        return ResponseEntity.ok(abrigoService.listar());
    }
}