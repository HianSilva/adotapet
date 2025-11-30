package br.edu.ifrn.hiansil.adotapet.repository;

import br.edu.ifrn.hiansil.adotapet.model.SolicitacaoAdocaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoAdocaoRepository extends JpaRepository<SolicitacaoAdocaoModel, Long> {
    List<SolicitacaoAdocaoModel> findByAnimalAbrigoId(Long abrigoId);

    boolean existsByAdotanteIdAndAnimalIdAndStatus(Long adotanteId, Long animalId, String status);
}