package br.edu.ifrn.hiansil.adotapet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "abrigos")
public class AbrigoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String documento;

    private String telefone;

    private String senha;
}