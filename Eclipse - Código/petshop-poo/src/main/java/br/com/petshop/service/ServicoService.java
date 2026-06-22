package br.com.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Servico;
import br.com.petshop.repository.AtendimentoRepository;
import br.com.petshop.repository.ServicoRepository;

/**
 * Service responsavel pelas regras de negocio relacionadas aos servicos.
 *
 * Esta classe organiza o cadastro, a pesquisa e a manutencao dos servicos
 * oferecidos pelo pet shop. A regra de inativacao preserva o historico de
 * atendimentos, evitando exclusao definitiva de servicos ja utilizados.
 */
@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final AtendimentoRepository atendimentoRepository;

    public ServicoService(ServicoRepository servicoRepository, AtendimentoRepository atendimentoRepository) {
        this.servicoRepository = servicoRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return servicoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Transactional(readOnly = true)
    public List<Servico> listarAtivos() {
        return servicoRepository.findByAtivoTrueOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<Servico> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }

        return servicoRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
    }

    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico nao encontrado."));
    }

    @Transactional
    public Servico salvar(Servico servico) {
        prepararDados(servico);
        validarNomeUnico(servico);

        if (servico.getAtivo() == null) {
            servico.setAtivo(true);
        }

        return servicoRepository.save(servico);
    }

    @Transactional
    public void inativar(Long id) {
        Servico servico = buscarPorId(id);
        servico.inativar();
        servicoRepository.save(servico);
    }

    @Transactional
    public void reativar(Long id) {
        Servico servico = buscarPorId(id);
        servico.reativar();
        servicoRepository.save(servico);
    }

    @Transactional
    public void excluirOuInativar(Long id) {
        Servico servico = buscarPorId(id);

        if (atendimentoRepository.existsByServicoId(id)) {
            servico.inativar();
            servicoRepository.save(servico);
            return;
        }

        servicoRepository.delete(servico);
    }

    private void prepararDados(Servico servico) {
        servico.setNome(servico.getNome().trim());

        if (servico.getDescricao() != null) {
            servico.setDescricao(servico.getDescricao().trim());
        }
    }

    private void validarNomeUnico(Servico servico) {
        Optional<Servico> servicoCadastrado = servicoRepository.findByNomeIgnoreCase(servico.getNome());

        Long idAtual = servico.getId();

        if (servicoCadastrado.isPresent()
                && (idAtual == null || !servicoCadastrado.get().getId().equals(idAtual))) {
            throw new RegraNegocioException("Ja existe um servico cadastrado com este nome.");
        }
    }
}