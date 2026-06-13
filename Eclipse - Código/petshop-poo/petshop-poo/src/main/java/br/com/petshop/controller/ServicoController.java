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
import br.com.petshop.model.Servico;
import br.com.petshop.service.ServicoService;
import jakarta.validation.Valid;

/**
 * Controller responsavel pelo modulo de servicos do sistema.
 *
 * Esta classe coordena as operacoes de listagem, cadastro, edicao e exclusao
 * logica ou definitiva dos servicos oferecidos pelo pet shop. Na arquitetura
 * MVC, o controller atua como ponto de entrada das requisicoes da interface
 * web, delegando as regras de negocio para a camada Service.
 */
@Controller
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @GetMapping
    public String listar(@RequestParam(name = "nome", required = false) String nome, Model model) {
        model.addAttribute("servicos", servicoService.pesquisarPorNome(nome));
        model.addAttribute("nome", nome);
        return "servicos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        Servico servico = new Servico();
        servico.setAtivo(true);

        model.addAttribute("servico", servico);
        prepararFormulario(model, "Novo serviço", "Cadastre um procedimento oferecido pelo pet shop.");
        return "servicos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("servico", servicoService.buscarPorId(id));
        prepararFormulario(model, "Editar serviço", "Atualize os dados do serviço selecionado.");
        return "servicos/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Servico servico, BindingResult resultado, Model model,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            prepararFormulario(model, servico.getId() == null ? "Novo serviço" : "Editar serviço",
                    "Corrija os campos indicados para concluir o cadastro.");
            return "servicos/formulario";
        }

        try {
            servicoService.salvar(servico);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Serviço salvo com sucesso.");
            return "redirect:/servicos";
        } catch (RegraNegocioException exception) {
            resultado.reject("erroRegraNegocio", exception.getMessage());
            prepararFormulario(model, servico.getId() == null ? "Novo serviço" : "Editar serviço",
                    "Revise os dados informados antes de salvar.");
            return "servicos/formulario";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicoService.excluirOuInativar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    "Serviço removido ou inativado com sucesso.");
        } catch (RegraNegocioException | RecursoNaoEncontradoException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }

        return "redirect:/servicos";
    }

    @PostMapping("/reativar/{id}")
    public String reativar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicoService.reativar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Serviço reativado com sucesso.");
        } catch (RegraNegocioException | RecursoNaoEncontradoException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }

        return "redirect:/servicos";
    }

    private void prepararFormulario(Model model, String tituloPagina, String subtituloPagina) {
        model.addAttribute("tituloPagina", tituloPagina);
        model.addAttribute("subtituloPagina", subtituloPagina);
    }
}