package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AdotanteRequestDTO;
import br.edu.ifrn.hiansil.adotapet.model.AdotanteModel;
import br.edu.ifrn.hiansil.adotapet.repository.AdotanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // Status nasce como "ATIVO" por padrão no Model

        adotanteRepository.save(adotante);
    }
}