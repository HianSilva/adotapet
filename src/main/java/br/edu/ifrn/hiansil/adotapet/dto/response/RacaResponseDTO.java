package br.edu.ifrn.hiansil.adotapet.dto.response;

import br.edu.ifrn.hiansil.adotapet.model.RacaModel;
import lombok.Data;

@Data
public class RacaResponseDTO {
    private Long id;
    private String nome;
    private String especie; // Ex: "Gato"

    public RacaResponseDTO(RacaModel raca) {
        this.id = raca.getId();
        this.nome = raca.getNome();
        this.especie = raca.getEspecie().getNome();
    }
}