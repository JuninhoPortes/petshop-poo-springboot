package br.com.petshop.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.petshop.model.Atendimento;

/**
 * Repository responsavel pelas operacoes de persistencia da entidade Atendimento.
 *
 * Esta interface fornece consultas voltadas ao historico de servicos prestados,
 * permitindo filtrar atendimentos por animal, servico, periodo e tutor. Esses
 * metodos serao fundamentais para cumprir os requisitos de consulta historica
 * e geracao de relatorios personalizados do sistema.
 */
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

    boolean existsByAnimalId(Long animalId);

    boolean existsByServicoId(Long servicoId);

    List<Atendimento> findByAnimalIdOrderByDataAtendimentoDesc(Long animalId);

    @Query("""
            SELECT a FROM Atendimento a
            JOIN FETCH a.animal an
            JOIN FETCH a.servico s
            JOIN FETCH an.proprietario p
            ORDER BY a.dataAtendimento DESC
            """)
    List<Atendimento> buscarTodosComRelacionamentos();

    @Query("""
            SELECT a FROM Atendimento a
            JOIN FETCH a.animal an
            JOIN FETCH a.servico s
            JOIN FETCH an.proprietario p
            WHERE (:animalId IS NULL OR an.id = :animalId)
            AND (:servicoId IS NULL OR s.id = :servicoId)
            AND (:dataInicio IS NULL OR a.dataAtendimento >= :dataInicio)
            AND (:dataFim IS NULL OR a.dataAtendimento <= :dataFim)
            ORDER BY a.dataAtendimento DESC
            """)
    List<Atendimento> filtrarHistorico(
            @Param("animalId") Long animalId,
            @Param("servicoId") Long servicoId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    @Query("""
            SELECT a FROM Atendimento a
            JOIN FETCH a.animal an
            JOIN FETCH a.servico s
            JOIN FETCH an.proprietario p
            WHERE p.id = :proprietarioId
            AND a.dataAtendimento BETWEEN :dataInicio AND :dataFim
            ORDER BY a.dataAtendimento ASC, an.nome ASC
            """)
    List<Atendimento> buscarPorProprietarioEPeriodo(
            @Param("proprietarioId") Long proprietarioId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);
}