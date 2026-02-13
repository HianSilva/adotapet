package br.edu.ifrn.hiansil.adotapet.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para operações de autenticação.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta de autenticação com token de acesso")
public class TokenResponseDTO {

    @Schema(description = "Token de acesso JWT", example = "eyJhbGciOiJSUzI1NiIsInR5cCI...")
    private String accessToken;

    @Schema(description = "Token de atualização", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...")
    private String refreshToken;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tokenType;

    @Schema(description = "Tempo de expiração do token em segundos", example = "300")
    private Long expiresIn;
}
