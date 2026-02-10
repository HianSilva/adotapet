package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AdotanteRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AdotanteResponseDTO;
import br.edu.ifrn.hiansil.adotapet.model.AdotanteModel;
import br.edu.ifrn.hiansil.adotapet.repository.AdotanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdotanteService {

    private final AdotanteRepository adotanteRepository;

    @Transactional
    public void cadastrar(AdotanteRequestDTO dto) {
        if (adotanteRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        if (adotanteRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        AdotanteModel adotante = new AdotanteModel();
        adotante.setNome(dto.getNome());
        adotante.setEmail(dto.getEmail());
        adotante.setCpf(dto.getCpf());
        adotante.setEndereco(dto.getEndereco());

        adotanteRepository.save(adotante);
    }

    public List<AdotanteResponseDTO> listar() {
        return adotanteRepository.findAll().stream()
                .map(AdotanteResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AdotanteResponseDTO obterPorId(Long id) {
        AdotanteModel adotante = adotanteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado."));
        return new AdotanteResponseDTO(adotante);
    }

    public void bloquearAdotante(Long id) {
        AdotanteModel adotante = adotanteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado."));
        adotante.setStatus("BLOQUEADO");
        adotanteRepository.save(adotante);
    }

    public void desbloquearAdotante(Long id) {
        AdotanteModel adotante = adotanteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado."));
        adotante.setStatus("ATIVO");
        adotanteRepository.save(adotante);
    }

    public void deletarAdotante(Long id) {
        if (!adotanteRepository.existsById(id)) {
            throw new IllegalArgumentException("Adotante não encontrado.");
        }
        adotanteRepository.deleteById(id);
    }
}