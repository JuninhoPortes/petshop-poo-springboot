package br.com.petshop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Animal;
import br.com.petshop.model.Proprietario;
import br.com.petshop.repository.AnimalRepository;
import br.com.petshop.repository.AtendimentoRepository;
import br.com.petshop.repository.ProprietarioRepository;

/**
 * Service responsavel pelas regras de negocio relacionadas aos animais.
 *
 * Esta classe controla o cadastro, a edicao, a consulta e a exclusao de pets,
 * garantindo que cada animal esteja vinculado a um tutor existente. Tambem
 * protege o historico operacional do sistema ao impedir a remocao de animais
 * que ja possuem atendimentos registrados.
 */
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ProprietarioRepository proprietarioRepository;
    private final AtendimentoRepository atendimentoRepository;

    public AnimalService(AnimalRepository animalRepository, ProprietarioRepository proprietarioRepository,
            AtendimentoRepository atendimentoRepository) {
        this.animalRepository = animalRepository;
        this.proprietarioRepository = proprietarioRepository;
        this.atendimentoRepository = atendimentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Animal> listarTodos() {
        return animalRepository.buscarTodosComProprietario();
    }

    @Transactional(readOnly = true)
    public List<Animal> pesquisarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listarTodos();
        }

        return animalRepository.buscarPorNomeComProprietario(nome.trim());
    }

    @Transactional(readOnly = true)
    public List<Animal> listarPorProprietario(Long proprietarioId) {
        return animalRepository.findByProprietarioIdOrderByNomeAsc(proprietarioId);
    }

    @Transactional(readOnly = true)
    public Animal buscarPorId(Long id) {
        return animalRepository.buscarPorIdComProprietario(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Animal nao encontrado."));
    }

    @Transactional
    public Animal salvar(Animal animal) {
        prepararDados(animal);

        if (animal.getProprietario() == null || animal.getProprietario().getId() == null) {
            throw new RegraNegocioException("O animal deve estar vinculado a um tutor cadastrado.");
        }

        Proprietario proprietario = proprietarioRepository.findById(animal.getProprietario().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tutor informado nao encontrado."));

        animal.setProprietario(proprietario);

        return animalRepository.save(animal);
    }

    @Transactional
    public void excluir(Long id) {
        Animal animal = buscarPorId(id);

        if (atendimentoRepository.existsByAnimalId(id)) {
            throw new RegraNegocioException(
                    "Nao e possivel excluir o animal, pois existem atendimentos registrados para ele.");
        }

        animalRepository.delete(animal);
    }

    private void prepararDados(Animal animal) {
        animal.setNome(limparTexto(animal.getNome()));
        animal.setEspecie(limparTexto(animal.getEspecie()));
        animal.setRaca(limparTexto(animal.getRaca()));

        String fotoCaminho = limparTexto(animal.getFotoCaminho());
        animal.setFotoCaminho(fotoCaminho == null || fotoCaminho.isBlank() ? null : fotoCaminho);
    }

    private String limparTexto(String valor) {
        return valor == null ? null : valor.trim();
    }
}