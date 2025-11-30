package br.edu.ifrn.hiansil.adotapet.repository;

import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbrigoRepository extends JpaRepository<AbrigoModel, Long> {
    boolean existsByEmail(String email);
    boolean existsByDocumento(String documento);
    boolean existsByNome(String nome);
}