package br.edu.ifrn.hiansil.adotapet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "adotantes")
public class AdotanteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private String endereco;

    @Column (nullable = false)
    private String senha;

    // "ATIVO" ou "BLOQUEADO"
    @Column(nullable = false)
    private String status = "ATIVO";
}