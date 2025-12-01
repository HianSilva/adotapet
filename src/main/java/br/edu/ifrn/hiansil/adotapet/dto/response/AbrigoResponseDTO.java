package br.edu.ifrn.hiansil.adotapet.dto.response;

import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import lombok.Data;

@Data
public class AbrigoResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String documento;
    private String telefone;

    public AbrigoResponseDTO(AbrigoModel abrigo) {
        this.id = abrigo.getId();
        this.nome = abrigo.getNome();
        this.email = abrigo.getEmail();
        this.documento = abrigo.getDocumento();
        this.telefone = abrigo.getTelefone();
    }
}