package br.com.petshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.petshop.model.Animal;

/**
 * Repository responsavel pelas operacoes de persistencia da entidade Animal.
 *
 * Esta interface permite consultar animais cadastrados, pesquisar pets por nome
 * e recuperar registros com os dados do tutor responsavel ja carregados. Essa
 * abordagem evita acessos tardios a relacionamentos durante a renderizacao das
 * telas, mantendo maior controle sobre a camada de persistencia.
 */
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByProprietarioIdOrderByNomeAsc(Long proprietarioId);

    boolean existsByProprietarioId(Long proprietarioId);

    @Query("SELECT a FROM Animal a JOIN FETCH a.proprietario ORDER BY a.nome ASC")
    List<Animal> buscarTodosComProprietario();

    @Query("""
            SELECT a FROM Animal a
            JOIN FETCH a.proprietario p
            WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))
            ORDER BY a.nome ASC
            """)
    List<Animal> buscarPorNomeComProprietario(@Param("nome") String nome);

    @Query("""
            SELECT a FROM Animal a
            JOIN FETCH a.proprietario
            WHERE a.id = :id
            """)
    Optional<Animal> buscarPorIdComProprietario(@Param("id") Long id);
}