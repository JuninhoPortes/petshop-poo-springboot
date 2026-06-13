package br.com.petshop.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Animal;
import br.com.petshop.model.Atendimento;
import br.com.petshop.model.Servico;
import br.com.petshop.service.AnimalService;
import br.com.petshop.service.AtendimentoService;
import br.com.petshop.service.ServicoService;
import jakarta.validation.Valid;

/**
 * Controller responsavel pelo modulo de atendimentos do sistema.
 *
 * Esta classe coordena o lancamento, a listagem e a exclusao de servicos
 * prestados aos animais cadastrados. No fluxo MVC, ela recebe as requisicoes da
 * interface web, prepara os dados necessarios para os formularios e delega as
 * regras de negocio para a camada Service.
 */
@Controller
@RequestMapping("/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;
    private final AnimalService animalService;
    private final ServicoService servicoService;

    public AtendimentoController(AtendimentoService atendimentoService, AnimalService animalService,
            ServicoService servicoService) {
        this.atendimentoService = atendimentoService;
        this.animalService = animalService;
        this.servicoService = servicoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("atendimentos", atendimentoService.listarTodos());
        return "atendimentos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Atendimento atendimento = new Atendimento();
        atendimento.setDataAtendimento(LocalDate.now());
        atendimento.setAnimal(new Animal());
        atendimento.setServico(new Servico());

        model.addAttribute("atendimento", atendimento);
        prepararFormulario(model, "Novo atendimento",
                "Registre o serviço prestado a um animal cadastrado no sistema.");

        return "atendimentos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Atendimento atendimento, BindingResult resultado, Model model,
            RedirectAttributes redirectAttributes) {
        garantirRelacionamentos(atendimento);

        if (resultado.hasErrors()) {
            prepararFormulario(model, "Novo atendimento",
                    "Corrija os campos indicados para concluir o lançamento.");
            return "atendimentos/formulario";
        }

        try {
            atendimentoService.registrar(atendimento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Atendimento registrado com sucesso.");
            return "redirect:/atendimentos";
        } catch (RegraNegocioException | RecursoNaoEncontradoException exception) {
            resultado.reject("erroRegraNegocio", exception.getMessage());
            prepararFormulario(model, "Novo atendimento", "Revise os dados informados antes de salvar.");
            return "atendimentos/formulario";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            atendimentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Atendimento excluído com sucesso.");
        } catch (RecursoNaoEncontradoException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }

        return "redirect:/atendimentos";
    }

    private void prepararFormulario(Model model, String tituloPagina, String subtituloPagina) {
        model.addAttribute("tituloPagina", tituloPagina);
        model.addAttribute("subtituloPagina", subtituloPagina);
        model.addAttribute("animais", animalService.listarTodos());
        model.addAttribute("servicos", servicoService.listarAtivos());
        model.addAttribute("dataAtual", LocalDate.now());
    }

    private void garantirRelacionamentos(Atendimento atendimento) {
        if (atendimento.getAnimal() == null) {
            atendimento.setAnimal(new Animal());
        }

        if (atendimento.getServico() == null) {
            atendimento.setServico(new Servico());
        }
    }
}