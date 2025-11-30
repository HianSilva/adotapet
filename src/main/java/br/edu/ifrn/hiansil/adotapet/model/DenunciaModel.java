package br.edu.ifrn.hiansil.adotapet.model;

import br.edu.ifrn.hiansil.adotapet.model.enums.StatusDenunciaEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "denuncias")
public class DenunciaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_denuncia")
    private LocalDateTime dataDenuncia = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Enumerated(EnumType.STRING)
    private StatusDenunciaEnum status;

    @ManyToOne
    @JoinColumn(name = "adotante_id", nullable = false)
    private AdotanteModel adotante;

    @ManyToOne
    @JoinColumn(name = "abrigo_id", nullable = false)
    private AbrigoModel abrigo;
}