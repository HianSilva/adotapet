package br.edu.ifrn.hiansil.adotapet.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "especies")
public class EspecieModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;
}