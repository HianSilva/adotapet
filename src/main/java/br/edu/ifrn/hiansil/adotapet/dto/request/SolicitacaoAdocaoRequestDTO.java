package br.edu.ifrn.hiansil.adotapet.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitacaoAdocaoRequestDTO {
    @NotNull
    private Long animalId;

    @NotNull
    private Long adotanteId;

    @AssertTrue(message = "É necessário aceitar o termo de responsabilidade")
    private boolean termoAceito;
}