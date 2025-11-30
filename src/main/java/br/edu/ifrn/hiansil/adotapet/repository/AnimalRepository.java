package br.edu.ifrn.hiansil.adotapet.repository;

import br.edu.ifrn.hiansil.adotapet.model.AnimalModel;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalModel, Long> {
    List<AnimalModel> findByStatusAdocao(StatusAdocaoEnum status);
    List<AnimalModel> findByAbrigoId(Long abrigoId);
}