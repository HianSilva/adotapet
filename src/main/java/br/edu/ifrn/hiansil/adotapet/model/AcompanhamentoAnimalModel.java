package br.edu.ifrn.hiansil.adotapet.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "acompanhamentos")
public class AcompanhamentoAnimalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_acompanhamento")
    private LocalDateTime dataAcompanhamento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "contato_realizado")
    private boolean contatoRealizado;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalModel animal;

    @ManyToOne
    @JoinColumn(name = "abrigo_id", nullable = false)
    private AbrigoModel abrigo;
}