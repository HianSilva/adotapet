package br.edu.ifrn.hiansil.adotapet.dto.response;

import br.edu.ifrn.hiansil.adotapet.model.AnimalModel;
import br.edu.ifrn.hiansil.adotapet.model.enums.PorteAnimalEnum;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import lombok.Data;

@Data
public class AnimalResponseDTO {
    private Long id;
    private String nome;
    private Integer idadeMeses;
    private PorteAnimalEnum porte;
    private String descricao;
    private StatusAdocaoEnum status;
    private RacaResponseDTO raca;
    private String nomeAbrigo;

    public AnimalResponseDTO(AnimalModel animal) {
        this.id = animal.getId();
        this.nome = animal.getNome();
        this.idadeMeses = animal.getIdadeEstimadaMeses();
        this.porte = animal.getPorte();
        this.descricao = animal.getDescricao();
        this.status = animal.getStatusAdocao();
        this.raca = new RacaResponseDTO(animal.getRaca());
        this.nomeAbrigo = animal.getAbrigo().getNome();
    }
}