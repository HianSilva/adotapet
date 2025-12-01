package br.edu.ifrn.hiansil.adotapet.dto.request;

import br.edu.ifrn.hiansil.adotapet.model.enums.PorteAnimalEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimalRequestDTO {
    @NotBlank(message = "Nome do animal é obrigatório")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade não pode ser negativa")
    private Integer idadeEstimadaMeses;

    @NotNull(message = "Porte é obrigatório (PEQUENO, MEDIO, GRANDE)")
    private PorteAnimalEnum porte;

    private String descricao;

    @NotNull(message = "ID do Abrigo é obrigatório")
    private Long abrigoId;

    @NotNull(message = "ID da Raça é obrigatório")
    private Long racaId;
}