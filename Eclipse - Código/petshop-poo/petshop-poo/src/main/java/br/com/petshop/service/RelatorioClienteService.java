package br.com.petshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.petshop.dto.RelatorioClienteDTO;
import br.com.petshop.dto.TotalPorDataDTO;
import br.com.petshop.dto.TotalPorServicoDTO;
import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Animal;
import br.com.petshop.model.Atendimento;
import br.com.petshop.model.Proprietario;
import br.com.petshop.repository.AnimalRepository;
import br.com.petshop.repository.AtendimentoRepository;
import br.com.petshop.repository.ProprietarioRepository;

/**
 * Service responsavel pela composicao dos relatorios personalizados por cliente.
 *
 * Esta classe concentra as regras de consulta e consolidacao dos atendimentos
 * realizados para um tutor em determinado periodo. Alem de buscar os dados no
 * banco, ela calcula totais por servico, totais por data e valor geral do
 * relatorio, mantendo essa logica fora da camada Controller.
 */
@Service
public class RelatorioClienteService {

    private final ProprietarioRepository proprietarioRepository;
    private final AnimalRepository animalRepository;
    private final AtendimentoRepository atendimentoRepository;

    public RelatorioClienteService(ProprietarioRepository proprietarioRepository, AnimalRepository animalRepository,
            AtendimentoRepository atendimentoRepository) {
        this.proprietarioRepository = proprietarioRepository;
        this.animalRepository = animalRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    @Transactional(readOnly = true)
    public RelatorioClienteDTO gerarRelatorio(Long proprietarioId, LocalDate dataInicio, LocalDate dataFim) {
        validarParametros(proprietarioId, dataInicio, dataFim);

        Proprietario proprietario = proprietarioRepository.findById(proprietarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor informado nao encontrado."));

        List<Animal> animais = animalRepository.findByProprietarioIdOrderByNomeAsc(proprietarioId);

        List<Atendimento> atendimentos = atendimentoRepository.buscarPorProprietarioEPeriodo(
                proprietarioId, dataInicio, dataFim);

        BigDecimal valorTotal = calcularValorTotal(atendimentos);

        return new RelatorioClienteDTO(
                proprietario,
                animais,
                atendimentos,
                calcularTotaisPorServico(atendimentos),
                calcularTotaisPorData(atendimentos),
                valorTotal,
                dataInicio,
                dataFim);
    }

    private void validarParametros(Long proprietarioId, LocalDate dataInicio, LocalDate dataFim) {
        if (proprietarioId == null) {
            throw new RegraNegocioException("Selecione um tutor para gerar o relatorio.");
        }

        if (dataInicio == null || dataFim == null) {
            throw new RegraNegocioException("Informe a data inicial e a data final do periodo.");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new RegraNegocioException("A data inicial nao pode ser posterior a data final.");
        }
    }

    private BigDecimal calcularValorTotal(List<Atendimento> atendimentos) {
        return atendimentos.stream()
                .map(Atendimento::getValorCobrado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<TotalPorServicoDTO> calcularTotaisPorServico(List<Atendimento> atendimentos) {
        Map<String, List<Atendimento>> agrupados = atendimentos.stream()
                .collect(Collectors.groupingBy(
                        atendimento -> atendimento.getServico().getNome(),
                        TreeMap::new,
                        Collectors.toList()));

        return agrupados.entrySet().stream()
                .map(entry -> new TotalPorServicoDTO(
                        entry.getKey(),
                        (long) entry.getValue().size(),
                        calcularValorTotal(entry.getValue())))
                .toList();
    }

    private List<TotalPorDataDTO> calcularTotaisPorData(List<Atendimento> atendimentos) {
        Map<LocalDate, List<Atendimento>> agrupados = atendimentos.stream()
                .collect(Collectors.groupingBy(
                        Atendimento::getDataAtendimento,
                        TreeMap::new,
                        Collectors.toList()));

        return agrupados.entrySet().stream()
                .map(entry -> new TotalPorDataDTO(
                        entry.getKey(),
                        (long) entry.getValue().size(),
                        calcularValorTotal(entry.getValue())))
                .toList();
    }
}