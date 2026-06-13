package br.com.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.petshop.model.Proprietario;

/**
 * Repository responsavel pelas operacoes de persistencia da entidade Proprietario.
 *
 * Esta interface fornece os recursos basicos de cadastro, consulta, atualizacao
 * e remocao de tutores do pet shop. Alem dos metodos herdados de JpaRepository,
 * foram definidos metodos especificos para pesquisa por nome e verificacao de
 * e-mail, permitindo consultas mais adequadas ao funcionamento do sistema.
 */
public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {

    List<Proprietario> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    boolean existsByEmail(String email);
}