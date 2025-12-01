package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AbrigoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AbrigoResponseDTO;
import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Injeção de dependência automática do Lombok
public class AbrigoService {

    private final AbrigoRepository abrigoRepository;

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
        abrigo.setSenha(dto.getSenha());

        abrigoRepository.save(abrigo);

        return new AbrigoResponseDTO(abrigo);
    }

    public List<AbrigoResponseDTO> listar() {
        return abrigoRepository.findAll().stream()
                .map(AbrigoResponseDTO::new)
                .collect(Collectors.toList());
    }
}