# **Gerenciador Investimento**
Aplicação web desenvolvida em Java com Spring Boot que possibilita o cadastro, gerenciamento e consulta de ativos de investimento, integrando informações de usuários, contas e ações.
O sistema foi projetado com arquitetura em camadas, integração com banco de dados MySQL via Hibernate/JPA e suporte a testes automatizados.
O projeto utiliza containers Docker para o banco de dados, ferramentas de inspeção de API como Insomnia e administração de banco com MySQL Workbench, proporcionando um ambiente completo de desenvolvimento.

## Funções
Cadastro e gerenciamento de usuários.
Cadastro e vinculação de contas a usuários.
Registro de ações (stocks) e associação de ações a contas.
Operações CRUD completas.
Integração com API REST documentada.
Persistência de dados em MySQL com mapeamento objeto-relacional.

## Estrutura do Projeto

```plaintext
gerenciador-investimento/
 ├── src/
 │    ├── main/
 │    │    ├── java/com/spring/gerenciadorinvestimento/
 │    │    │     ├── controller/       #  Endpoints REST
 │    │    │     ├── dto/              #  Objetos de Transferência de Dados (DTOs)
 │    │    │     ├── entity/           #  Entidades JPA (mapeamento de tabelas)
 │    │    │     ├── repository/       #  Interfaces JPA Repository
 │    │    │     ├── service/          #  Regras de negócio
 │    │    │     └── exception/        #  Tratamento de erros e exceções
 │    │    └── resources/
 │    │          ├── application.properties  #  Configurações do Spring Boot
 │    │          └── data.sql / schema.sql   #  Scripts de inicialização do banco
 │    └── test/                              #  Testes automatizados
 ├── Dockerfile                              #  Build da imagem da aplicação
 ├── docker-compose.yml                      #  Orquestração de containers
 ├── pom.xml                                 #  Configuração do Maven
 └── README.md                               #  Documentação do projeto
```

## Organização

- Separação clara entre camadas.
- Uso de DTOs para manter a segurança e integridade dos dados.
- Entities desacopladas da lógica de negócios.
- Arquivos de configuração e scripts SQL centralizados em resources.
- Estrutura que facilita testes, manutenção e expansão.

## Métodos e Padrões Utilizados 
- Spring Boot – Framework principal.
- Spring Data JPA – Acesso a dados.
- Hibernate – Mapeamento objeto-relacional.
- DTO (Data Transfer Object) – Transferência segura de dados.
- Service Layer Pattern – Regras de negócio separadas da camada de controle.
- Repository Pattern – Abstração de persistência.
- Bean Validation – Validação automática de dados de entrada.
- Exception Handling – Respostas consistentes para erros.
- Docker Compose – Orquestração do MySQL.
- Testes com JUnit e Mockito – Garantia de funcionamento da lógica.

## Testes
- JUnit 5 – Estrutura principal de testes.
- Mockito – Simulação de dependências.
- Spring Boot Test – Testes de integração.
- Testes existentes cobrem:
- Serviços (regras de negócio).
- Repositórios (operações no banco).
- Integração (fluxos completos).

## Ferramentas Utilizadas
- Docker Desktop – Criação e execução do container MySQL.
- MySQL Workbench – Administração e consulta de banco de dados.
- Insomnia – Testes de requisições HTTP da API.

## Utilização

- `docker-compose up`
  
  <img width="929" height="44" alt="docker desk" src="https://github.com/user-attachments/assets/2d60407f-441c-4bd4-b721-676203280eaa" />
  
- Run GerenciadorinvestimentoApplication

  <img width="369" height="22" alt="run" src="https://github.com/user-attachments/assets/7334d2f0-f060-4c61-afab-efcc9a05c845" />

- Indicado utilar o Insomnia
  
  <img width="562" height="132" alt="Posts gets" src="https://github.com/user-attachments/assets/7f56b45a-4fc9-4977-995e-4520a38ad224" />
  
> | Post | Get | Put | Del |

1. Criar Usuário
     - POST: BODY - JSON -  `{"username": "Name", "email": "email@mail.com", "password": "0001"}`
2. Verificando Usuário
     - Buscar um usuário
     - GET: `http://localhost:8080/v1/users/{id do usuário}`
     - Buscar todos usuários
     - GET: `http://localhost:8080/v1/users`
3. Criar Conta
     - POST: BODY - JSON -  `{"description": "Conta de Investimento", "street": "Rua", "number": "100"}`
4. Verificando Conta
     - Buscar todas contas
     - GET: `http://localhost:8080/v1/users/{id do usuário}/accounts`
5. Anexar Conta Stock
     - `http://localhost:8080/v1/accounts/{id da conta}/stocks`
     - POST: BODY - JSON -  `{"stockId": "MGLU3", "quantity": "10"}`
6. Listar Stocks Conta
     - GET: `http://localhost:8080/v1/accounts/{id da conta}/accounts`

<img width="748" height="242" alt="stock" src="https://github.com/user-attachments/assets/356db55a-e30e-4831-80c9-95b3726467c6" />

> Demostração do exemplo acima de 10 ações da magalu atualizadas, utilizando brapi

> Usuário > Conta > Stock: 
> Um usuário pode ter diversas contas e somente a conta recebe as Stocks e seus valores
