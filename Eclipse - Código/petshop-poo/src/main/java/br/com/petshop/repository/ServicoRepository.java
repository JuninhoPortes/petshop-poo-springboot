package br.com.petshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.petshop.model.Servico;

/**
 * Repository responsavel pelas operacoes de persistencia da entidade Servico.
 *
 * Esta interface concentra os metodos de acesso aos servicos oferecidos pelo
 * pet shop. A separacao entre servicos ativos e inativos permite preservar o
 * historico de atendimentos sem excluir definitivamente servicos que ja foram
 * utilizados em lancamentos anteriores.
 */
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    List<Servico> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Servico> findByAtivoTrueOrderByNomeAsc();

    Optional<Servico> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}