package br.edu.ifrn.hiansil.adotapet.model;

import br.edu.ifrn.hiansil.adotapet.model.enums.StatusSolicitacaoEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitacoes_adocao")
public class SolicitacaoAdocaoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacaoEnum status = StatusSolicitacaoEnum.PENDENTE;

    @Column(name = "termo_responsabilidade_aceito", nullable = false)
    private boolean termoResponsabilidadeAceito;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalModel animal;

    @ManyToOne
    @JoinColumn(name = "adotante_id", nullable = false)
    private AdotanteModel adotante;
}