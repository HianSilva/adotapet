# AdotaPet API

API RESTful desenvolvida como parte da disciplina de **Desenvolvimento de Sistemas Corporativos (ADS)**. O objetivo é gerenciar o processo de adoção de animais, conectando abrigos e adotantes de forma segura e eficiente.

## Tecnologias

- **Java 17** & **Spring Boot 3**
- **Spring Data JPA** (Persistência)
- **PostgreSQL** (Banco de Dados)
- **Bean Validation** (Regras de validação)
- **Docker & Docker Compose** (Containerização)
- **JUnit 5 & Mockito** (Testes Unitários)

## Arquitetura

O projeto segue uma arquitetura em camadas visando desacoplamento e fácil manutenção:
- **Controller:** Interface REST.
- **Service:** Regras de negócio e transações.
- **Repository:** Acesso a dados.
- **Model:** Entidades JPA.

## Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados.

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone [https://github.com/seu-usuario/adotapet-api.git](https://github.com/seu-usuario/adotapet-api.git)
   ```
2. Configure as Variáveis de Ambiente: O projeto utiliza um arquivo .env para configurações sensíveis (Banco de Dados).

    Crie o arquivo .env na raiz do projeto baseando-se no exemplo fornecido:
    
    Linux/Mac:
    ```
        cp .env.example .env
    ```
    Windows (PowerShell):
    ```
        copy .env.example .env
    ```
    
    Abra o arquivo .env gerado e preencha as variáveis conforme necessário. 

3. Suba o ambiente (API + Banco):
   ```docker-compose up --build```

4. API estará disponível em: ```http://localhost:8080/api/v1```
