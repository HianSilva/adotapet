package br.edu.ifrn.hiansil.adotapet.model;

import br.edu.ifrn.hiansil.adotapet.model.enums.PorteAnimalEnum;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "animais")
public class AnimalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "idade_estimada_meses")
    private Integer idadeEstimadaMeses;

    @Enumerated(EnumType.STRING)
    @Column(name="porte", nullable=false)
    private PorteAnimalEnum porte;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name= "status_adocao", nullable = false)
    private StatusAdocaoEnum statusAdocao = StatusAdocaoEnum.DISPONIVEL;

    @ManyToOne
    @JoinColumn(name = "abrigo_id", nullable = false)
    private AbrigoModel abrigo;

    @ManyToOne
    @JoinColumn(name = "raca_id", nullable = false)
    private RacaModel raca;

    @ManyToOne
    @JoinColumn(name = "especie_id", nullable = false)
    private EspecieModel especie;
}