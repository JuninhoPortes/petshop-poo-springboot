# Sistema PetShop POO

Sistema web desenvolvido para gerenciamento de um pet shop, com foco na aplicação dos conceitos de Programação Orientada a Objetos, arquitetura MVC, persistência em banco de dados MySQL e interface gráfica amigável.

O projeto permite cadastrar tutores, animais, serviços, registrar atendimentos realizados, consultar histórico de serviços por animal e gerar relatórios personalizados por cliente e período, incluindo exportação em PDF.

## Tecnologias utilizadas

* Java 17+
* Spring Boot
* Spring Web MVC
* Thymeleaf
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* OpenPDF
* HTML5
* CSS3
* JavaScript

## Arquitetura do projeto

O sistema foi estruturado seguindo o padrão MVC, separando responsabilidades entre as camadas de modelo, controle, serviço, repositório e visualização.

Estrutura principal:

```text
src/main/java/br/com/petshop
├── controller
├── dto
├── exception
├── model
├── repository
├── service
└── PetshopPooApplication.java
```

Principais responsabilidades:

```text
model       -> entidades do domínio
repository  -> acesso ao banco de dados
service     -> regras de negócio
controller  -> controle das rotas e integração com as telas
dto         -> objetos auxiliares para relatórios
templates   -> telas Thymeleaf
static/css  -> estilos da interface
```

## Funcionalidades implementadas

### Tutores

* Cadastro de tutores
* Edição de tutores
* Listagem de tutores
* Pesquisa por nome
* Exclusão com validação de vínculo com animais

### Animais

* Cadastro de animais vinculados a tutores
* Edição de animais
* Listagem de animais
* Pesquisa por nome
* Exclusão com validação de atendimentos vinculados
* Campo opcional para URL de foto do animal

### Serviços

* Cadastro de serviços
* Edição de serviços
* Listagem de serviços
* Pesquisa por nome
* Controle de status ativo/inativo
* Validação de preço
* Inativação automática quando o serviço possui atendimentos vinculados

### Atendimentos

* Registro de serviços prestados a animais cadastrados
* Seleção de animal e serviço
* Valor cobrado editável
* Data do atendimento
* Observações opcionais
* Listagem dos atendimentos registrados
* Exclusão de atendimentos

### Histórico de serviços

* Consulta de histórico por animal
* Filtro por tipo de serviço
* Filtro por período
* Total de atendimentos encontrados
* Valor total filtrado
* Exibição dos resultados em tabela

### Relatórios

* Relatório personalizado por tutor
* Filtro por cliente e período
* Dados cadastrais do cliente
* Animais vinculados ao cliente
* Detalhamento dos serviços prestados
* Total por serviço
* Total por data
* Valor total no período
* Exportação do relatório em PDF

## Banco de dados

O sistema utiliza MySQL como banco de dados relacional.

O banco configurado no projeto é:

```text
petshop_poo
```

A configuração padrão está em:

```text
src/main/resources/application.properties
```

Configuração utilizada:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petshop_poo?useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

Caso o MySQL local utilize senha, altere a propriedade:

```properties
spring.datasource.password=
```

Exemplo:

```properties
spring.datasource.password=sua_senha
```

## Script SQL para facilitar os testes

Foi disponibilizado um script SQL com criação do banco, criação das tabelas e dados iniciais de teste.

Arquivo:

```text
Banco - Para facilitar o teste/banco_petshop_poo.sql
```

Esse script recria as tabelas e insere dados de exemplo para facilitar a validação do sistema.

Atenção: o script possui comandos `DROP TABLE`, portanto ele apaga os dados existentes das tabelas antes de recriá-las.

### Como executar o script pelo MySQL Workbench

1. Abra o MySQL Workbench.
2. Entre na conexão local do MySQL.
3. Vá em `File > Open SQL Script...`.
4. Selecione o arquivo `banco_petshop_poo.sql`.
5. Execute o script pelo botão de raio ou pelo atalho `Ctrl + Shift + Enter`.
6. Após executar, confira se as tabelas foram criadas corretamente.

Comando para conferência:

```sql
USE petshop_poo;

SELECT * FROM proprietarios;
SELECT * FROM animais;
SELECT * FROM servicos;
SELECT * FROM atendimentos;
```

## Como executar o projeto no Eclipse

### Pré-requisitos

Antes de executar, é necessário ter instalado:

* Java 17 ou superior
* Eclipse IDE
* Maven
* MySQL Server
* MySQL Workbench ou outro cliente SQL

### Passos para execução

1. Abra o Eclipse.
2. Importe o projeto como projeto Maven.
3. Aguarde o Maven baixar as dependências.
4. Verifique se o MySQL está em execução.
5. Execute o script `banco_petshop_poo.sql`, caso deseje iniciar com dados de teste.
6. Confira o arquivo `application.properties` e ajuste usuário/senha do MySQL se necessário.
7. Execute a classe principal:

```text
PetshopPooApplication.java
```

8. Acesse o sistema no navegador:

```text
http://localhost:8080
```

## Como testar o sistema

Após iniciar a aplicação, recomenda-se validar os módulos na seguinte ordem:

1. Acessar a tela inicial.
2. Consultar tutores cadastrados.
3. Cadastrar um novo tutor.
4. Cadastrar um animal vinculado a um tutor.
5. Cadastrar ou editar um serviço.
6. Registrar um atendimento para um animal.
7. Consultar o histórico de serviços por animal.
8. Gerar relatório por cliente e período.
9. Exportar o relatório em PDF.

Para testar relatórios com os dados do script, utilize um período que inclua as datas dos atendimentos cadastrados, por exemplo:

```text
03/06/2026 até 13/06/2026
```

## Teste de imagens dos animais

O campo de foto do animal é opcional e recebe uma URL pública de imagem.

É possível deixar esse campo vazio. Nesse caso, o sistema exibirá um avatar com a inicial do nome do animal.

Para testar com imagem real, pode ser utilizada uma URL pública de imagem de animais. Uma opção simples é pesquisar imagens no Unsplash:

```text
https://unsplash.com
```

Sugestão de uso:

1. Acesse o Unsplash.
2. Pesquise por termos como `dog`, `cat`, `pet` ou `puppy`.
3. Abra uma imagem.
4. Copie o endereço público da imagem.
5. Cole no campo `URL da foto` ao cadastrar ou editar um animal.

Também é possível utilizar URLs diretas como as presentes no script SQL de teste:

```text
https://images.unsplash.com/photo-1552053831-71594a27632d
https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba
https://images.unsplash.com/photo-1583337130417-3346a1be7dee
```

O uso de imagem é apenas visual e não é obrigatório para o funcionamento do cadastro.

## Exportação de relatório em PDF

O sistema possui exportação de relatório em PDF utilizando a biblioteca OpenPDF.

Para testar:

1. Acesse o menu `Relatórios`.
2. Selecione um tutor.
3. Informe a data inicial e a data final.
4. Clique em `Gerar relatório`.
5. Após o relatório aparecer na tela, clique em `Exportar PDF`.
6. O navegador fará o download do arquivo PDF.

O PDF contém:

* Dados do cliente
* Período consultado
* Animais vinculados
* Serviços prestados
* Total por serviço
* Total por data
* Valor total do período

## Regras e validações implementadas

O sistema possui validações para manter a consistência dos dados, como:

* Tutor deve possuir nome, endereço e telefone.
* Animal deve estar vinculado a um tutor.
* Serviço deve possuir nome e preço válido.
* Atendimento deve estar vinculado a um animal e a um serviço.
* Serviços inativos não podem ser utilizados em novos atendimentos.
* Tutores com animais vinculados não podem ser excluídos diretamente.
* Animais com atendimentos vinculados não podem ser excluídos diretamente.
* Serviços com atendimentos vinculados são inativados em vez de removidos.
* Relatórios exigem tutor e período válido.
* Data inicial não pode ser posterior à data final.

## Observações sobre segurança e consistência

O projeto utiliza validações na camada de serviço para impedir operações inválidas e preservar a integridade das informações.

Também foram utilizadas chaves estrangeiras no banco de dados para manter o relacionamento entre tutores, animais, serviços e atendimentos.

## Principais entidades do sistema

### Proprietario

Representa o tutor ou cliente do pet shop.

Campos principais:

```text
id
nome
endereco
telefone
email
```

### Animal

Representa o animal atendido pelo pet shop.

Campos principais:

```text
id
nome
especie
raca
idade
sexo
peso
fotoCaminho
proprietario
```

### Servico

Representa um serviço oferecido pelo pet shop.

Campos principais:

```text
id
nome
descricao
precoBase
ativo
```

### Atendimento

Representa o lançamento de um serviço realizado para um animal.

Campos principais:

```text
id
dataAtendimento
animal
servico
valorCobrado
observacoes
```

## Organização visual

A interface foi construída com Thymeleaf, HTML e CSS, utilizando um layout administrativo com menu lateral, cards, tabelas, formulários e mensagens de retorno.

O objetivo foi manter uma navegação clara entre os módulos:

```text
Painel
Tutores
Animais
Serviços
Atendimentos
Histórico
Relatórios
```

## Dependências principais

As principais dependências Maven utilizadas são:

```text
spring-boot-starter-web
spring-boot-starter-thymeleaf
spring-boot-starter-data-jpa
spring-boot-starter-validation
mysql-connector-j
openpdf
spring-boot-starter-test
```

## Execução via terminal

Também é possível executar o projeto pelo terminal, dentro da pasta raiz do projeto:

```bash
mvn spring-boot:run
```

Após iniciar, acesse:

```text
http://localhost:8080
```

## Autor

Projeto desenvolvido como atividade acadêmica da disciplina de Programação Orientada a Objetos.

Curso: Ciência da Computação<br>
Instituição: UNIPAC Barbacena
