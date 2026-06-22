package br.com.petshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Animal;
import br.com.petshop.model.Atendimento;
import br.com.petshop.model.Proprietario;
import br.com.petshop.model.Servico;
import br.com.petshop.repository.AnimalRepository;
import br.com.petshop.repository.AtendimentoRepository;
import br.com.petshop.repository.ProprietarioRepository;
import br.com.petshop.repository.ServicoRepository;

/**
 * Service responsavel pelas regras de negocio relacionadas aos atendimentos.
 *
 * Esta classe registra a prestacao de servicos aos animais cadastrados,
 * relacionando data, pet, servico, valor cobrado e observacoes. Tambem fornece
 * consultas de historico por animal, tipo de servico, periodo e tutor, servindo
 * de base para os relatorios personalizados do sistema.
 */
@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final AnimalRepository animalRepository;
    private final ServicoRepository servicoRepository;
    private final ProprietarioRepository proprietarioRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository, AnimalRepository animalRepository,
            ServicoRepository servicoRepository, ProprietarioRepository proprietarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.animalRepository = animalRepository;
        this.servicoRepository = servicoRepository;
        this.proprietarioRepository = proprietarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Atendimento> listarTodos() {
        return atendimentoRepository.buscarTodosComRelacionamentos();
    }

    @Transactional(readOnly = true)
    public Atendimento buscarPorId(Long id) {
        return atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento nao encontrado."));
    }

    @Transactional(readOnly = true)
    public List<Atendimento> listarHistoricoPorAnimal(Long animalId) {
        return atendimentoRepository.findByAnimalIdOrderByDataAtendimentoDesc(animalId);
    }

    @Transactional(readOnly = true)
    public List<Atendimento> filtrarHistorico(Long animalId, Long servicoId, LocalDate dataInicio,
            LocalDate dataFim) {
        validarPeriodo(dataInicio, dataFim);
        return atendimentoRepository.filtrarHistorico(animalId, servicoId, dataInicio, dataFim);
    }

    @Transactional(readOnly = true)
    public List<Atendimento> buscarPorProprietarioEPeriodo(Long proprietarioId, LocalDate dataInicio,
            LocalDate dataFim) {
        validarPeriodoObrigatorio(dataInicio, dataFim);

        Proprietario proprietario = proprietarioRepository.findById(proprietarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor informado nao encontrado."));

        return atendimentoRepository.buscarPorProprietarioEPeriodo(proprietario.getId(), dataInicio, dataFim);
    }

    @Transactional
    public Atendimento registrar(Atendimento atendimento) {
        if (atendimento.getAnimal() == null || atendimento.getAnimal().getId() == null) {
            throw new RegraNegocioException("Informe o animal atendido.");
        }

        if (atendimento.getServico() == null || atendimento.getServico().getId() == null) {
            throw new RegraNegocioException("Informe o servico executado.");
        }

        Animal animal = animalRepository.findById(atendimento.getAnimal().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal informado nao encontrado."));

        Servico servico = servicoRepository.findById(atendimento.getServico().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico informado nao encontrado."));

        if (!Boolean.TRUE.equals(servico.getAtivo())) {
            throw new RegraNegocioException("Nao e possivel registrar atendimento para um servico inativo.");
        }

        if (atendimento.getDataAtendimento() == null) {
            atendimento.setDataAtendimento(LocalDate.now());
        }

        if (atendimento.getValorCobrado() == null) {
            atendimento.setValorCobrado(servico.getPrecoBase());
        }

        validarValor(atendimento.getValorCobrado());

        atendimento.setAnimal(animal);
        atendimento.setServico(servico);

        if (atendimento.getObservacoes() != null) {
            atendimento.setObservacoes(atendimento.getObservacoes().trim());
        }

        return atendimentoRepository.save(atendimento);
    }

    @Transactional
    public void excluir(Long id) {
        Atendimento atendimento = buscarPorId(id);
        atendimentoRepository.delete(atendimento);
    }

    private void validarValor(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraNegocioException("O valor cobrado deve ser maior que zero.");
        }
    }

    private void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            throw new RegraNegocioException("A data inicial nao pode ser posterior a data final.");
        }
    }

    private void validarPeriodoObrigatorio(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new RegraNegocioException("Informe a data inicial e a data final.");
        }

        validarPeriodo(dataInicio, dataFim);
    }
}