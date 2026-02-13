package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de Abrigos.
 * A autenticação é gerenciada pelo Keycloak via OAuth2/JWT.
 */
@Service
@RequiredArgsConstructor
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AbrigoResponseDTO cadastrar(AbrigoRequestDTO dto) {
        if (abrigoRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um abrigo cadastrado com esse e-mail.");
        }
        if (abrigoRepository.existsByDocumento(dto.getDocumento())) {
            throw new IllegalArgumentException("Já existe um abrigo cadastrado com esse documento.");
        }

        AbrigoModel abrigo = new AbrigoModel();
        abrigo.setNome(dto.getNome());
        abrigo.setEmail(dto.getEmail());
        abrigo.setDocumento(dto.getDocumento());
        abrigo.setTelefone(dto.getTelefone());
        abrigo.setSenha(passwordEncoder.encode(dto.getSenha()));

        abrigoRepository.save(abrigo);

        return new AbrigoResponseDTO(abrigo);
    }

    public List<AbrigoResponseDTO> listar() {
        return abrigoRepository.findAll().stream()
                .map(AbrigoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AbrigoResponseDTO buscarPorEmail(String email) {
        AbrigoModel abrigo = abrigoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Abrigo não encontrado com email: " + email));
        return new AbrigoResponseDTO(abrigo);
    }
}