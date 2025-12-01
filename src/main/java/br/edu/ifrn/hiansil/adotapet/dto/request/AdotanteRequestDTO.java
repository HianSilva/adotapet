package br.edu.ifrn.hiansil.adotapet.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import lombok.Data;

@Data
public class AdotanteRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @CPF
    private String cpf;

    private String endereco;
}