package br.edu.ifrn.hiansil.adotapet.repository;

import br.edu.ifrn.hiansil.adotapet.model.RacaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RacaRepository extends JpaRepository<RacaModel, Long> {
    List<RacaModel> findByEspecieId(long especieId);
}