package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.SolicitacaoAdocaoRequestDTO;
import br.edu.ifrn.hiansil.adotapet.model.AdotanteModel;
import br.edu.ifrn.hiansil.adotapet.model.AnimalModel;
import br.edu.ifrn.hiansil.adotapet.model.SolicitacaoAdocaoModel;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusSolicitacaoEnum;
import br.edu.ifrn.hiansil.adotapet.repository.AdotanteRepository;
import br.edu.ifrn.hiansil.adotapet.repository.AnimalRepository;
import br.edu.ifrn.hiansil.adotapet.repository.SolicitacaoAdocaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolicitacaoAdocaoService {

    private final SolicitacaoAdocaoRepository solicitacaoRepository;
    private final AnimalRepository animalRepository;
    private final AdotanteRepository adotanteRepository;

    @Transactional
    public void solicitar(SolicitacaoAdocaoRequestDTO dto) {
        AnimalModel animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("Animal não encontrado."));

        AdotanteModel adotante = adotanteRepository.findById(dto.getAdotanteId())
                .orElseThrow(() -> new IllegalArgumentException("Adotante não encontrado."));

        // REGRA 1: Animal deve estar disponível
        if (animal.getStatusAdocao() != StatusAdocaoEnum.DISPONIVEL) {
            throw new IllegalArgumentException("Este animal não está disponível para adoção.");
        }

        // REGRA 2: Adotante não pode estar bloqueado (Blacklist)
        if ("BLOQUEADO".equals(adotante.getStatus())) {
            throw new IllegalArgumentException("Adotante bloqueado por denúncias anteriores.");
        }

        // REGRA 3: Não pode solicitar duas vezes o mesmo animal (opcional, mas boa prática)
        boolean jaSolicitou = solicitacaoRepository.existsByAdotanteIdAndAnimalIdAndStatus(
                adotante.getId(), animal.getId(), String.valueOf(StatusSolicitacaoEnum.PENDENTE));
        if (jaSolicitou) {
            throw new IllegalArgumentException("Você já tem uma solicitação pendente para este animal.");
        }

        SolicitacaoAdocaoModel solicitacao = new SolicitacaoAdocaoModel();
        solicitacao.setAnimal(animal);
        solicitacao.setAdotante(adotante);
        solicitacao.setTermoResponsabilidadeAceito(dto.isTermoAceito());
        solicitacao.setStatus(StatusSolicitacaoEnum.PENDENTE);

        solicitacaoRepository.save(solicitacao);
    }

    // Método para o Abrigo aprovar ou rejeitar
    @Transactional
    public void atualizarStatus(Long idSolicitacao, String novoStatus) {
        SolicitacaoAdocaoModel solicitacao = solicitacaoRepository.findById(idSolicitacao)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada."));

        if (String.valueOf(StatusSolicitacaoEnum.APROVADA).equalsIgnoreCase(novoStatus)) {

            solicitacao.setStatus(StatusSolicitacaoEnum.APROVADA);

            AnimalModel animal = solicitacao.getAnimal();
            animal.setStatusAdocao(StatusAdocaoEnum.ADOTADO);

            animalRepository.save(animal);

        } else if (String.valueOf(StatusSolicitacaoEnum.REJEITADA).equalsIgnoreCase(novoStatus)) {
            solicitacao.setStatus(StatusSolicitacaoEnum.REJEITADA);
        } else {
            throw new IllegalArgumentException("Status inválido. Use APROVADA ou REJEITADA.");
        }

        solicitacaoRepository.save(solicitacao);
    }
}