package br.edu.ifrn.hiansil.adotapet.dto.response;

import lombok.Data;
import br.edu.ifrn.hiansil.adotapet.model.AdotanteModel;

@Data
public class AdotanteResponseDTO {
    private String nome;

    private String email;

    private String cpf;

    private String endereco;

    public AdotanteResponseDTO(AdotanteModel adotante) {
        this.nome = adotante.getNome();
        this.email = adotante.getEmail();
        this.cpf = adotante.getCpf();
        this.endereco = adotante.getEndereco();
    }
}