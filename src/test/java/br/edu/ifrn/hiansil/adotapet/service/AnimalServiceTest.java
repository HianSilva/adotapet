package br.edu.ifrn.hiansil.adotapet.service;

import br.edu.ifrn.hiansil.adotapet.dto.request.AnimalRequestDTO;
import br.edu.ifrn.hiansil.adotapet.dto.response.AnimalResponseDTO;
import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.model.AnimalModel;
import br.edu.ifrn.hiansil.adotapet.model.EspecieModel;
import br.edu.ifrn.hiansil.adotapet.model.RacaModel;
import br.edu.ifrn.hiansil.adotapet.model.enums.PorteAnimalEnum;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import br.edu.ifrn.hiansil.adotapet.repository.AnimalRepository;
import br.edu.ifrn.hiansil.adotapet.repository.RacaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnimalService - Testes Unitários")
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private RacaRepository racaRepository;

    @InjectMocks
    private AnimalService animalService;

    private AbrigoModel abrigo;
    private RacaModel raca;
    private EspecieModel especie;
    private AnimalModel animal;
    private AnimalRequestDTO animalRequestDTO;

    @BeforeEach
    void setUp() {
        especie = new EspecieModel();
        especie.setId(1L);
        especie.setNome("Cachorro");

        raca = new RacaModel();
        raca.setId(1L);
        raca.setNome("Labrador");
        raca.setEspecie(especie);

        abrigo = new AbrigoModel();
        abrigo.setId(1L);
        abrigo.setNome("Abrigo Feliz");
        abrigo.setEmail("abrigo@email.com");
        abrigo.setDocumento("12345678901234");
        abrigo.setTelefone("84999999999");

        animal = new AnimalModel();
        animal.setId(1L);
        animal.setNome("Rex");
        animal.setIdadeEstimadaMeses(24);
        animal.setPorte(PorteAnimalEnum.MEDIO);
        animal.setDescricao("Cachorro muito dócil");
        animal.setStatusAdocao(StatusAdocaoEnum.DISPONIVEL);
        animal.setAbrigo(abrigo);
        animal.setRaca(raca);

        animalRequestDTO = new AnimalRequestDTO();
        animalRequestDTO.setNome("Rex");
        animalRequestDTO.setIdadeEstimadaMeses(24);
        animalRequestDTO.setPorte(PorteAnimalEnum.MEDIO);
        animalRequestDTO.setDescricao("Cachorro muito dócil");
        animalRequestDTO.setAbrigoId(1L);
        animalRequestDTO.setRacaId(1L);
    }

    @Nested
    @DisplayName("Testes do método cadastrar()")
    class CadastrarTests {

        @Test
        @DisplayName("Deve cadastrar animal com sucesso quando dados válidos")
        void deveCadastrarAnimalComSucesso() {
            when(abrigoRepository.findById(1L)).thenReturn(Optional.of(abrigo));
            when(racaRepository.findById(1L)).thenReturn(Optional.of(raca));
            when(animalRepository.save(any(AnimalModel.class))).thenReturn(animal);

            AnimalResponseDTO resultado = animalService.cadastrar(animalRequestDTO);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getNome()).isEqualTo("Rex");
            assertThat(resultado.getPorte()).isEqualTo(PorteAnimalEnum.MEDIO);
            assertThat(resultado.getNomeAbrigo()).isEqualTo("Abrigo Feliz");
            
            verify(abrigoRepository).findById(1L);
            verify(racaRepository).findById(1L);
            verify(animalRepository).save(any(AnimalModel.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando abrigo não encontrado")
        void deveLancarExcecaoQuandoAbrigoNaoEncontrado() {
            when(abrigoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> animalService.cadastrar(animalRequestDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Abrigo não encontrado.");

            verify(abrigoRepository).findById(1L);
            verify(racaRepository, never()).findById(any());
            verify(animalRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando raça não encontrada")
        void deveLancarExcecaoQuandoRacaNaoEncontrada() {
            when(abrigoRepository.findById(1L)).thenReturn(Optional.of(abrigo));
            when(racaRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> animalService.cadastrar(animalRequestDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Raça não encontrada.");

            verify(abrigoRepository).findById(1L);
            verify(racaRepository).findById(1L);
            verify(animalRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método listarDisponiveis()")
    class ListarDisponiveisTests {

        @Test
        @DisplayName("Deve retornar lista de animais disponíveis")
        void deveRetornarListaDeAnimaisDisponiveis() {
            AnimalModel animal2 = new AnimalModel();
            animal2.setId(2L);
            animal2.setNome("Luna");
            animal2.setIdadeEstimadaMeses(12);
            animal2.setPorte(PorteAnimalEnum.PEQUENO);
            animal2.setStatusAdocao(StatusAdocaoEnum.DISPONIVEL);
            animal2.setAbrigo(abrigo);
            animal2.setRaca(raca);

            when(animalRepository.findByStatusAdocao(StatusAdocaoEnum.DISPONIVEL))
                    .thenReturn(Arrays.asList(animal, animal2));

            List<AnimalResponseDTO> resultado = animalService.listarDisponiveis();

            assertThat(resultado).hasSize(2);
            assertThat(resultado).extracting(AnimalResponseDTO::getNome)
                    .containsExactly("Rex", "Luna");
            
            verify(animalRepository).findByStatusAdocao(StatusAdocaoEnum.DISPONIVEL);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há animais disponíveis")
        void deveRetornarListaVaziaQuandoNaoHaAnimaisDisponiveis() {
            when(animalRepository.findByStatusAdocao(StatusAdocaoEnum.DISPONIVEL))
                    .thenReturn(Collections.emptyList());

            List<AnimalResponseDTO> resultado = animalService.listarDisponiveis();

            assertThat(resultado).isEmpty();
            verify(animalRepository).findByStatusAdocao(StatusAdocaoEnum.DISPONIVEL);
        }
    }

    @Nested
    @DisplayName("Testes do método listarTodos()")
    class ListarTodosTests {

        @Test
        @DisplayName("Deve retornar todos os animais independente do status")
        void deveRetornarTodosOsAnimais() {
            AnimalModel animalAdotado = new AnimalModel();
            animalAdotado.setId(2L);
            animalAdotado.setNome("Max");
            animalAdotado.setIdadeEstimadaMeses(36);
            animalAdotado.setPorte(PorteAnimalEnum.GRANDE);
            animalAdotado.setStatusAdocao(StatusAdocaoEnum.ADOTADO);
            animalAdotado.setAbrigo(abrigo);
            animalAdotado.setRaca(raca);

            when(animalRepository.findAll()).thenReturn(Arrays.asList(animal, animalAdotado));

            List<AnimalResponseDTO> resultado = animalService.listarTodos();

            assertThat(resultado).hasSize(2);
            assertThat(resultado).extracting(AnimalResponseDTO::getStatus)
                    .containsExactly(StatusAdocaoEnum.DISPONIVEL, StatusAdocaoEnum.ADOTADO);
            
            verify(animalRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método obterPorId()")
    class ObterPorIdTests {

        @Test
        @DisplayName("Deve retornar animal quando encontrado")
        void deveRetornarAnimalQuandoEncontrado() {
            when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

            AnimalResponseDTO resultado = animalService.obterPorId(1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNome()).isEqualTo("Rex");
            
            verify(animalRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando animal não encontrado")
        void deveLancarExcecaoQuandoAnimalNaoEncontrado() {
            when(animalRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> animalService.obterPorId(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Animal não encontrado.");

            verify(animalRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("Testes do método deletarAnimal()")
    class DeletarAnimalTests {

        @Test
        @DisplayName("Deve deletar animal quando existe")
        void deveDeletarAnimalQuandoExiste() {
            when(animalRepository.existsById(1L)).thenReturn(true);
            doNothing().when(animalRepository).deleteById(1L);

            animalService.deletarAnimal(1L);

            verify(animalRepository).existsById(1L);
            verify(animalRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar animal inexistente")
        void deveLancarExcecaoAoDeletarAnimalInexistente() {
            when(animalRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> animalService.deletarAnimal(99L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Animal não encontrado.");

            verify(animalRepository).existsById(99L);
            verify(animalRepository, never()).deleteById(any());
        }
    }
}
