# Desafio MsiTecnologia

Este é um projeto de backend desenvolvido em Java utilizando o framework Spring Boot. O projeto implementa um sistema de gerenciamento de usuários com autenticação e autorização.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- JWT (JSON Web Token)
- Maven

## Configuração do Projeto

### Banco de Dados

O projeto utiliza o banco de dados H2 em memória para desenvolvimento e testes. As configurações do banco de dados estão no arquivo `application.properties`.

### Segurança

A segurança do projeto é gerenciada pelo Spring Security, utilizando JWT para autenticação. As chaves pública e privada para JWT devem ser trocadas, ambas estão localizadas nos arquivos `app.key` e `app.pub`.

## Endpoints

### Autenticação

- **POST /auth/login**: Autentica um usuário e retorna um token JWT.
  - **Request Body**: 
    ```json
    {
      "email": "usuario@example.com",
      "senha": "senha123"
    }
    ```
  - **Response**: Token JWT

- **POST /auth/register**: Registra um novo usuário.
- A role **admin** tem acesso a realizar todas as operações, a role **user** não pode excluir e nem inativar usuários e por fim a role **deleted** não tem permissão a realizar login ou requisições para o sistema.
- **ROLE_DELETED** é a role quando o usuário é inativado por um administrador.
  - **Request Body**: 
    ```json
    {
      "nome": "Nome do Usuário",
      "email": "usuario@example.com",
      "dataNascimento": "1990-01-01",
      "senha": "senha123",
      "role": "admin"
    }
    ```
  - **Response**: Dados do usuário registrado

### Usuários

- **GET /usuarios?page=1**: Lista todos os usuários com paginação.
  - **Query Param**: `page` (opcional, padrão é 1)
  - **Response**: Lista de usuários

- **GET /usuarios/{id}**: Busca um usuário pelo ID.
  - **Response**: Dados do usuário

- **PUT /usuarios/{id}**: Atualiza os dados de um usuário.
- Usuários com a permissão **"ROLE_ADMIN"** podem alterar a role de outros usuários, caso um usuário que não seja administrador tentar realizar essa operação ele irá enviar uma mensagem de exeption.
  - **Request Body**: 
    ```json
    {
      "nome": "Nome Atualizado",
      "email": "usuario@example.com",
      "dataNascimento": "1990-01-01",
      "senha": "novaSenha123",
      "role": "USER"
    }
    ```
  - **Response**: Dados do usuário atualizado

- **POST /usuarios/inativar/{id}**: Inativa um usuário (apenas para ADMIN).
  - **Response**: Mensagem de sucesso

- **DELETE /usuarios/excluir/{id}**: Exclui um usuário (apenas para ADMIN).
  - **Response**: Mensagem de sucesso

## Exceções e Tratamento de Erros

O projeto possui um manipulador global de exceções que retorna respostas apropriadas para diferentes tipos de erros, como validação de dados e autenticação.

## Como Executar

1. Clone o repositório.
2. Navegue até o diretório do projeto.

3. O banco de dados já está criado ao realizar build do projeto, as informações estão em: `src\main\resources\application.properties`. Como o banco é local e não possui senha, deixei fora do .gitignore, para facilitar a build do projeto.
4. As chaves de segurança são criadas automaticamente quando executa o `docker-compose up --build`
   
5. No diretório raiz, execute o `docker-compose up --build`.
6. Acesse `http://localhost:8080` para interagir com a API.
7. É importante lembrar que somente as url's: **/auth/login e /auth/register** estão liberadas sem o token jwt.
8. Para realizar as requisições para os demais endpoint's deve ser adicionado no Header `Authorization` e o value `Baerer SeuTokenAoRealizarLogin`.

**IMPORTANTE:** Segue abaixo os comandos para realizar a criação da chave pública e privada caso tenha algum erro ao criar com o **docker**:
- Crie o `app.key e app.pub`
- cd caminho/para/seu/projeto
- Gerar chave privada: openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out app.key 
- Extrair chave pública: openssl rsa -pubout -in app.key -out app.pub

9. Visualização do banco de dados: `http://localhost:8080/h2-console/`
10. As informações para login estão em: `src\main\resources\application.properties`
