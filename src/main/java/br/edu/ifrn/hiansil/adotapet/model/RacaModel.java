package br.edu.ifrn.hiansil.adotapet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "racas")
public class RacaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private TipoEspecie especie; // CACHORRO, GATO...
}