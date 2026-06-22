package br.com.petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Animal;
import br.com.petshop.model.Proprietario;
import br.com.petshop.model.SexoAnimal;
import br.com.petshop.service.AnimalService;
import br.com.petshop.service.ProprietarioService;
import jakarta.validation.Valid;

/**
 * Controller responsavel pelo modulo de animais do sistema.
 *
 * Esta classe coordena as operacoes de listagem, cadastro, edicao e exclusao
 * dos animais atendidos pelo pet shop. Na arquitetura MVC, ela recebe as
 * requisicoes da interface web, delega as regras de negocio para a camada
 * Service e retorna os templates Thymeleaf correspondentes.
 */
@Controller
@RequestMapping("/animais")
public class AnimalController {

    private final AnimalService animalService;
    private final ProprietarioService proprietarioService;

    public AnimalController(AnimalService animalService, ProprietarioService proprietarioService) {
        this.animalService = animalService;
        this.proprietarioService = proprietarioService;
    }

    @GetMapping
    public String listar(@RequestParam(name = "nome", required = false) String nome, Model model) {
        model.addAttribute("animais", animalService.pesquisarPorNome(nome));
        model.addAttribute("nome", nome);
        return "animais/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Animal animal = new Animal();
        animal.setProprietario(new Proprietario());

        model.addAttribute("animal", animal);
        prepararFormulario(model, "Novo animal", "Preencha os dados para vincular um pet a um tutor cadastrado.");
        return "animais/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("animal", animalService.buscarPorId(id));
        prepararFormulario(model, "Editar animal", "Atualize os dados cadastrais do animal selecionado.");
        return "animais/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Animal animal, BindingResult resultado, Model model,
            RedirectAttributes redirectAttributes) {
        if (animal.getProprietario() == null) {
            animal.setProprietario(new Proprietario());
        }

        if (resultado.hasErrors()) {
            prepararFormulario(model, animal.getId() == null ? "Novo animal" : "Editar animal",
                    "Corrija os campos indicados para concluir o cadastro.");
            return "animais/formulario";
        }

        try {
            animalService.salvar(animal);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal salvo com sucesso.");
            return "redirect:/animais";
        } catch (RegraNegocioException | RecursoNaoEncontradoException exception) {
            resultado.reject("erroRegraNegocio", exception.getMessage());
            prepararFormulario(model, animal.getId() == null ? "Novo animal" : "Editar animal",
                    "Revise os dados informados antes de salvar.");
            return "animais/formulario";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            animalService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Animal excluido com sucesso.");
        } catch (RegraNegocioException | RecursoNaoEncontradoException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }

        return "redirect:/animais";
    }

    private void prepararFormulario(Model model, String tituloPagina, String subtituloPagina) {
        model.addAttribute("tituloPagina", tituloPagina);
        model.addAttribute("subtituloPagina", subtituloPagina);
        model.addAttribute("proprietarios", proprietarioService.listarTodos());
        model.addAttribute("sexos", SexoAnimal.values());
    }
}