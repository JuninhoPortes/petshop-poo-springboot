/*
    Script de criacao e carga inicial do banco de dados do Sistema PetShop POO.

    Este arquivo prepara o banco MySQL utilizado pela aplicacao Spring Boot,
    recriando as tabelas principais e inserindo dados basicos para facilitar
    os testes de tutores, animais, servicos, atendimentos, historico e relatorios.
*/

CREATE DATABASE IF NOT EXISTS petshop_poo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE petshop_poo;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS atendimentos;
DROP TABLE IF EXISTS animais;
DROP TABLE IF EXISTS servicos;
DROP TABLE IF EXISTS proprietarios;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE proprietarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    endereco VARCHAR(180) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(120),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE animais (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    especie VARCHAR(60) NOT NULL,
    raca VARCHAR(80),
    idade INT NOT NULL,
    sexo ENUM('MACHO', 'FEMEA') NOT NULL,
    peso DECIMAL(6,2) NOT NULL,
    foto_caminho VARCHAR(255),
    proprietario_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_animais_proprietarios
        FOREIGN KEY (proprietario_id)
        REFERENCES proprietarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE servicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500),
    preco_base DECIMAL(7,2) NOT NULL,
    ativo BIT NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE atendimentos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    data_atendimento DATE NOT NULL,
    animal_id BIGINT NOT NULL,
    servico_id BIGINT NOT NULL,
    valor_cobrado DECIMAL(10,2) NOT NULL,
    observacoes VARCHAR(500),
    PRIMARY KEY (id),
    CONSTRAINT fk_atendimentos_animais
        FOREIGN KEY (animal_id)
        REFERENCES animais(id),
    CONSTRAINT fk_atendimentos_servicos
        FOREIGN KEY (servico_id)
        REFERENCES servicos(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO proprietarios (id, nome, endereco, telefone, email) VALUES
(1, 'Mariana Souza', 'Rua das Acacias, 120 - Centro - Barbacena/MG', '(32) 99911-2233', 'mariana.souza@email.com'),
(2, 'Carlos Henrique Lima', 'Avenida Bias Fortes, 455 - Sao Sebastiao - Barbacena/MG', '(32) 98844-5511', 'carlos.lima@email.com'),
(3, 'Fernanda Martins', 'Rua Primeiro de Maio, 87 - Santa Tereza - Barbacena/MG', '(32) 99777-1010', 'fernanda.martins@email.com');

INSERT INTO animais (id, nome, especie, raca, idade, sexo, peso, foto_caminho, proprietario_id) VALUES
(1, 'Thor', 'Cachorro', 'Golden Retriever', 4, 'MACHO', 28.50, 'https://images.unsplash.com/photo-1552053831-71594a27632d', 1),
(2, 'Maya', 'Gato', 'Siamês', 3, 'FEMEA', 4.20, 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba', 1),
(3, 'Luna', 'Cachorro', 'Shih-tzu', 2, 'FEMEA', 6.30, 'https://images.unsplash.com/photo-1583337130417-3346a1be7dee', 2),
(4, 'Bob', 'Cachorro', 'Vira-lata', 6, 'MACHO', 13.80, NULL, 3);

INSERT INTO servicos (id, nome, descricao, preco_base, ativo) VALUES
(1, 'Banho completo', 'Higienização completa com shampoo adequado, secagem e escovação.', 79.90, b'1'),
(2, 'Tosa higiênica', 'Tosa das regiões de maior acúmulo de sujeira para conforto e higiene do animal.', 59.90, b'1'),
(3, 'Consulta veterinária', 'Avaliação clínica inicial realizada por profissional veterinário.', 120.00, b'1'),
(4, 'Corte de unhas', 'Corte cuidadoso das unhas do animal, respeitando o limite seguro.', 29.90, b'1'),
(5, 'Hidratação de pelagem', 'Tratamento de hidratação indicado para recuperação e brilho da pelagem.', 89.90, b'0');

INSERT INTO atendimentos (id, data_atendimento, animal_id, servico_id, valor_cobrado, observacoes) VALUES
(1, '2026-06-03', 1, 1, 79.90, 'Banho realizado sem intercorrências.'),
(2, '2026-06-05', 2, 3, 120.00, 'Consulta preventiva. Animal em bom estado geral.'),
(3, '2026-06-09', 3, 2, 59.90, 'Tosa higiênica realizada conforme solicitação do tutor.'),
(4, '2026-06-13', 1, 4, 29.90, 'Corte de unhas realizado após o banho.'),
(5, '2026-06-13', 4, 1, 74.90, 'Valor ajustado por promoção do dia.');