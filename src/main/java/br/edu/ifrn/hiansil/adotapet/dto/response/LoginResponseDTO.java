package br.edu.ifrn.hiansil.adotapet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private Long abrigoId;
    private String nome;
    private String email;
}
