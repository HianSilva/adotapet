package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AnimalRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AnimalResponseDTO;
import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.model.AnimalModel;
import br.edu.ifrn.hiansil.adotapet.model.RacaModel;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import br.edu.ifrn.hiansil.adotapet.repository.AnimalRepository;
import br.edu.ifrn.hiansil.adotapet.repository.RacaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AbrigoRepository abrigoRepository;
    private final RacaRepository racaRepository;

    @Transactional
    public AnimalResponseDTO cadastrar(AnimalRequestDTO dto) {
        AbrigoModel abrigo = abrigoRepository.findById(dto.getAbrigoId())
                .orElseThrow(() -> new IllegalArgumentException("Abrigo não encontrado."));

        RacaModel raca = racaRepository.findById(dto.getRacaId())
                .orElseThrow(() -> new IllegalArgumentException("Raça não encontrada."));

        AnimalModel animal = new AnimalModel();
        animal.setNome(dto.getNome());
        animal.setIdadeEstimadaMeses(dto.getIdadeEstimadaMeses());
        animal.setPorte(dto.getPorte());
        animal.setDescricao(dto.getDescricao());
        animal.setStatusAdocao(StatusAdocaoEnum.DISPONIVEL);
        animal.setAbrigo(abrigo);
        animal.setRaca(raca);

        animalRepository.save(animal);
        return new AnimalResponseDTO(animal);
    }

    public List<AnimalResponseDTO> listarDisponiveis() {
        return animalRepository.findByStatusAdocao(StatusAdocaoEnum.DISPONIVEL).stream()
                .map(AnimalResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<AnimalResponseDTO> listarTodos() {
        return animalRepository.findAll().stream()
                .map(AnimalResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<AnimalResponseDTO> listarPorAbrigo(Long abrigoId) {
        return animalRepository.findByAbrigoId(abrigoId).stream()
                .map(AnimalResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<AnimalResponseDTO> listarPorRaca(Long racaId) {
        return animalRepository.findByRacaId(racaId).stream()
                .map(AnimalResponseDTO::new)
                .collect(Collectors.toList());
    }

    public AnimalResponseDTO obterPorId(Long id) {
        AnimalModel animal = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Animal não encontrado."));
        return new AnimalResponseDTO(animal);
    }

    public void deletarAnimal(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new IllegalArgumentException("Animal não encontrado.");
        }
        animalRepository.deleteById(id);
    }

}