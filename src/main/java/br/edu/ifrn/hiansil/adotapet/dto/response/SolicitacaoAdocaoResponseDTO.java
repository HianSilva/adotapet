package br.edu.ifrn.hiansil.adotapet.dto.response;

import java.time.LocalDateTime;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusSolicitacaoEnum;
import lombok.Data;
import br.edu.ifrn.hiansil.adotapet.model.SolicitacaoAdocaoModel;
import br.edu.ifrn.hiansil.adotapet.dto.response.AnimalResponseDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AdotanteResponseDTO;

@Data
public class SolicitacaoAdocaoResponseDTO {
    private Long animalId;

    private Long adotanteId;

    private boolean termoAceito;

    private LocalDateTime dataSolicitacao;

    private StatusSolicitacaoEnum status;

    private boolean termoResponsabilidadeAceito;

    private AnimalResponseDTO animal;

    private AdotanteResponseDTO adotante;

    public SolicitacaoAdocaoResponseDTO(SolicitacaoAdocaoModel solicitacao) {
        this.animalId = solicitacao.getAnimal().getId();
        this.adotanteId = solicitacao.getAdotante().getId();
        this.termoAceito = solicitacao.isTermoResponsabilidadeAceito();
        this.dataSolicitacao = solicitacao.getDataSolicitacao();
        this.status = solicitacao.getStatus();
        this.animal = new AnimalResponseDTO(solicitacao.getAnimal());
        this.adotante = new AdotanteResponseDTO(solicitacao.getAdotante());
    }
}