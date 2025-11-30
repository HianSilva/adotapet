package br.edu.ifrn.hiansil.adotapet.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifrn.hiansil.adotapet.model.EspecieModel;

@Repository
public interface EspecieRepository extends JpaRepository<EspecieModel, Long> {
    boolean existisByNome(String nome);
}
