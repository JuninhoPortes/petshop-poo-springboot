package br.com.petshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.petshop.model.Animal;

/**
 * Repository responsavel pelas operacoes de persistencia da entidade Animal.
 *
 * Esta interface permite consultar animais cadastrados, pesquisar pets por nome
 * e recuperar os registros vinculados a um tutor especifico. A consulta com
 * carregamento do proprietario foi criada para facilitar futuras listagens na
 * interface, evitando acessos incompletos aos dados relacionais.
 */
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findByNomeContainingIgnoreCaseOrderByNomeAsc(String nome);

    List<Animal> findByProprietarioIdOrderByNomeAsc(Long proprietarioId);

    @Query("SELECT a FROM Animal a JOIN FETCH a.proprietario ORDER BY a.nome ASC")
    List<Animal> buscarTodosComProprietario();
}