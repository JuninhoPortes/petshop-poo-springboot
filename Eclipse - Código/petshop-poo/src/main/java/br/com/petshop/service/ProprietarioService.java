package br.com.petshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Proprietario;
import br.com.petshop.repository.AnimalRepository;
import br.com.petshop.repository.ProprietarioRepository;

/**
 * Service responsavel pelas regras de negocio relacionadas aos tutores.
 *
 * Esta classe organiza as operacoes de cadastro, pesquisa, atualizacao e
 * exclusao de proprietarios. Alem de acessar a camada Repository, ela executa
 * validacoes de negocio, como a verificacao de e-mail duplicado e a protecao
 * contra exclusao de tutores que ainda possuem animais vinculados.
 */
@Service
public class ProprietarioService {

    private final ProprietarioRepository proprietarioRepository;
    private final AnimalRepository animalRepository;

    public ProprietarioService(ProprietarioRepository proprietarioRepository, AnimalRepository animalRepository) {
        this.proprietarioRepository = proprietarioRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional(readOnly = true)
    public List<Proprietario> listarTodos() {
        return proprietarioRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

    @Transactional(readOnly = true)
    public List<Proprietario> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }

        return proprietarioRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
    }

    @Transactional(readOnly = true)
    public Proprietario buscarPorId(Long id) {
        return proprietarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor nao encontrado."));
    }

    @Transactional
    public Proprietario salvar(Proprietario proprietario) {
        prepararDados(proprietario);
        validarEmailUnico(proprietario);

        return proprietarioRepository.save(proprietario);
    }

    @Transactional
    public void excluir(Long id) {
        Proprietario proprietario = buscarPorId(id);

        if (animalRepository.existsByProprietarioId(id)) {
            throw new RegraNegocioException(
                    "Nao e possivel excluir o tutor, pois existem animais vinculados ao cadastro.");
        }

        proprietarioRepository.delete(proprietario);
    }

    private void prepararDados(Proprietario proprietario) {
        proprietario.setNome(proprietario.getNome().trim());
        proprietario.setEndereco(proprietario.getEndereco().trim());
        proprietario.setTelefone(proprietario.getTelefone().trim());

        if (proprietario.getEmail() != null) {
            proprietario.setEmail(proprietario.getEmail().trim());
        }
    }

    private void validarEmailUnico(Proprietario proprietario) {
        if (proprietario.getEmail() == null || proprietario.getEmail().isBlank()) {
            return;
        }

        Optional<Proprietario> proprietarioCadastrado = proprietarioRepository
                .findByEmailIgnoreCase(proprietario.getEmail());

        Long idAtual = proprietario.getId();

        if (proprietarioCadastrado.isPresent()
                && (idAtual == null || !proprietarioCadastrado.get().getId().equals(idAtual))) {
            throw new RegraNegocioException("Ja existe um tutor cadastrado com este e-mail.");
        }
    }
}