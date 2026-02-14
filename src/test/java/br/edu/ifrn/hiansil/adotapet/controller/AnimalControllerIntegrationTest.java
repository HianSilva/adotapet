package br.edu.ifrn.hiansil.adotapet.controller;

import br.edu.ifrn.hiansil.adotapet.dto.request.AnimalRequestDTO;
import br.edu.ifrn.hiansil.adotapet.model.AbrigoModel;
import br.edu.ifrn.hiansil.adotapet.model.AnimalModel;
import br.edu.ifrn.hiansil.adotapet.model.EspecieModel;
import br.edu.ifrn.hiansil.adotapet.model.RacaModel;
import br.edu.ifrn.hiansil.adotapet.model.enums.PorteAnimalEnum;
import br.edu.ifrn.hiansil.adotapet.model.enums.StatusAdocaoEnum;
import br.edu.ifrn.hiansil.adotapet.repository.AbrigoRepository;
import br.edu.ifrn.hiansil.adotapet.repository.AnimalRepository;
import br.edu.ifrn.hiansil.adotapet.repository.EspecieRepository;
import br.edu.ifrn.hiansil.adotapet.repository.RacaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@DisplayName("AnimalController - Testes de Integração")
class AnimalControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private ObjectMapper objectMapper;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private RacaRepository racaRepository;

    @Autowired
    private EspecieRepository especieRepository;

    private AbrigoModel abrigo;
    private RacaModel raca;
    private EspecieModel especie;
    private AnimalModel animal;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        animalRepository.deleteAll();
        racaRepository.deleteAll();
        especieRepository.deleteAll();
        abrigoRepository.deleteAll();

        especie = new EspecieModel();
        especie.setNome("Cachorro");
        especie = especieRepository.save(especie);

        raca = new RacaModel();
        raca.setNome("Labrador");
        raca.setEspecie(especie);
        raca = racaRepository.save(raca);

        abrigo = new AbrigoModel();
        abrigo.setNome("Abrigo Feliz");
        abrigo.setEmail("abrigo@email.com");
        abrigo.setDocumento("12345678901234");
        abrigo.setTelefone("84999999999");
        abrigo = abrigoRepository.save(abrigo);

        animal = new AnimalModel();
        animal.setNome("Rex");
        animal.setIdadeEstimadaMeses(24);
        animal.setPorte(PorteAnimalEnum.MEDIO);
        animal.setDescricao("Cachorro muito dócil");
        animal.setStatusAdocao(StatusAdocaoEnum.DISPONIVEL);
        animal.setAbrigo(abrigo);
        animal.setRaca(raca);
        animal = animalRepository.save(animal);
    }

    @Nested
    @DisplayName("GET /api/v1/animais/disponiveis - Endpoints Públicos")
    class ListarDisponiveisTests {

        @Test
        @DisplayName("Deve listar animais disponíveis sem autenticação")
        void deveListarAnimaisDisponiveisSemAutenticacao() throws Exception {
            ResultActions resultado = mockMvc.perform(get("/api/v1/animais/disponiveis")
                    .contentType(MediaType.APPLICATION_JSON));

            resultado.andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nome", is("Rex")))
                    .andExpect(jsonPath("$[0].status", is("DISPONIVEL")))
                    .andExpect(jsonPath("$[0].nomeAbrigo", is("Abrigo Feliz")));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há animais disponíveis")
        void deveRetornarListaVaziaQuandoNaoHaAnimais() throws Exception {
            animal.setStatusAdocao(StatusAdocaoEnum.ADOTADO);
            animalRepository.save(animal);

            mockMvc.perform(get("/api/v1/animais/disponiveis")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/animais/todos - Listar Todos")
    class ListarTodosTests {

        @Test
        @DisplayName("Deve listar todos os animais incluindo não disponíveis")
        void deveListarTodosOsAnimais() throws Exception {
            AnimalModel animalAdotado = new AnimalModel();
            animalAdotado.setNome("Luna");
            animalAdotado.setIdadeEstimadaMeses(12);
            animalAdotado.setPorte(PorteAnimalEnum.PEQUENO);
            animalAdotado.setStatusAdocao(StatusAdocaoEnum.ADOTADO);
            animalAdotado.setAbrigo(abrigo);
            animalAdotado.setRaca(raca);
            animalRepository.save(animalAdotado);

            // Act & Assert
            mockMvc.perform(get("/api/v1/animais/todos")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].nome", containsInAnyOrder("Rex", "Luna")));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/animais - Cadastrar Animal")
    class CadastrarTests {

        @Test
        @WithMockUser(roles = "ABRIGO")
        @DisplayName("Deve cadastrar animal com sucesso quando autenticado como ABRIGO")
        void deveCadastrarAnimalComSucesso() throws Exception {
            AnimalRequestDTO novoAnimal = new AnimalRequestDTO();
            novoAnimal.setNome("Max");
            novoAnimal.setIdadeEstimadaMeses(36);
            novoAnimal.setPorte(PorteAnimalEnum.GRANDE);
            novoAnimal.setDescricao("Cachorro amigável");
            novoAnimal.setAbrigoId(abrigo.getId());
            novoAnimal.setRacaId(raca.getId());

            // Act & Assert
            mockMvc.perform(post("/api/v1/animais")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(novoAnimal)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.nome", is("Max")))
                    .andExpect(jsonPath("$.porte", is("GRANDE")))
                    .andExpect(jsonPath("$.status", is("DISPONIVEL")));
        }

        @Test
        @DisplayName("Deve retornar 401 ao tentar cadastrar sem autenticação")
        void deveRetornar401AoCadastrarSemAutenticacao() throws Exception {
            AnimalRequestDTO novoAnimal = new AnimalRequestDTO();
            novoAnimal.setNome("Max");
            novoAnimal.setIdadeEstimadaMeses(36);
            novoAnimal.setPorte(PorteAnimalEnum.GRANDE);
            novoAnimal.setAbrigoId(abrigo.getId());
            novoAnimal.setRacaId(raca.getId());

            // Act & Assert
            mockMvc.perform(post("/api/v1/animais")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(novoAnimal)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(roles = "ABRIGO")
        @DisplayName("Deve retornar 400 ao cadastrar com dados inválidos")
        void deveRetornar400AoCadastrarComDadosInvalidos() throws Exception {
            AnimalRequestDTO animalInvalido = new AnimalRequestDTO();
            animalInvalido.setIdadeEstimadaMeses(24);
            animalInvalido.setPorte(PorteAnimalEnum.MEDIO);
            animalInvalido.setAbrigoId(abrigo.getId());
            animalInvalido.setRacaId(raca.getId());

            // Act & Assert
            mockMvc.perform(post("/api/v1/animais")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(animalInvalido)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/animais/{id} - Excluir Animal")
    class ExcluirTests {

        @Test
        @WithMockUser(roles = "ABRIGO")
        @DisplayName("Deve excluir animal com sucesso quando autenticado")
        void deveExcluirAnimalComSucesso() throws Exception {
            mockMvc.perform(delete("/api/v1/animais/{id}", animal.getId()))
                    .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/v1/animais/todos"))
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @DisplayName("Deve retornar 401 ao excluir sem autenticação")
        void deveRetornar401AoExcluirSemAutenticacao() throws Exception {
            mockMvc.perform(delete("/api/v1/animais/{id}", animal.getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/animais/abrigo/{abrigoId} - Listar por Abrigo")
    class ListarPorAbrigoTests {

        @Test
        @WithMockUser(roles = "ABRIGO")
        @DisplayName("Deve listar animais de um abrigo específico")
        void deveListarAnimaisDoAbrigo() throws Exception {
            mockMvc.perform(get("/api/v1/animais/abrigo/{abrigoId}", abrigo.getId())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].nomeAbrigo", is("Abrigo Feliz")));
        }
    }
}
