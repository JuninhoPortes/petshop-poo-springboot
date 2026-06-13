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

import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Proprietario;
import br.com.petshop.service.ProprietarioService;
import jakarta.validation.Valid;

/**
 * Controller responsavel pelo modulo de tutores do sistema.
 *
 * Esta classe coordena as operacoes de listagem, cadastro, edicao e exclusao
 * de proprietarios. No contexto da arquitetura MVC, ela recebe as requisicoes
 * da interface web, aciona a camada de servico e direciona o usuario para os
 * templates Thymeleaf correspondentes.
 *
 * A separacao entre controller, service e repository permite que a interface
 * permaneça simples, enquanto as regras de negocio continuam concentradas na
 * camada apropriada.
 */
@Controller
@RequestMapping("/proprietarios")
public class ProprietarioController {

    private final ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    @GetMapping
    public String listar(@RequestParam(name = "nome", required = false) String nome, Model model) {
        model.addAttribute("proprietarios", proprietarioService.pesquisarPorNome(nome));
        model.addAttribute("nome", nome);
        return "proprietarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("proprietario", new Proprietario());
        model.addAttribute("tituloPagina", "Novo tutor");
        model.addAttribute("subtituloPagina", "Preencha os dados para cadastrar um novo tutor no sistema.");
        return "proprietarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("proprietario", proprietarioService.buscarPorId(id));
        model.addAttribute("tituloPagina", "Editar tutor");
        model.addAttribute("subtituloPagina", "Atualize os dados cadastrais do tutor selecionado.");
        return "proprietarios/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Proprietario proprietario, BindingResult resultado, Model model,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            prepararRetornoFormulario(proprietario, model);
            return "proprietarios/formulario";
        }

        try {
            proprietarioService.salvar(proprietario);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor salvo com sucesso.");
            return "redirect:/proprietarios";
        } catch (RegraNegocioException exception) {
            resultado.reject("erroRegraNegocio", exception.getMessage());
            prepararRetornoFormulario(proprietario, model);
            return "proprietarios/formulario";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proprietarioService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Tutor excluido com sucesso.");
        } catch (RegraNegocioException exception) {
            redirectAttributes.addFlashAttribute("mensagemErro", exception.getMessage());
        }

        return "redirect:/proprietarios";
    }

    private void prepararRetornoFormulario(Proprietario proprietario, Model model) {
        boolean edicao = proprietario.getId() != null;

        model.addAttribute("tituloPagina", edicao ? "Editar tutor" : "Novo tutor");
        model.addAttribute("subtituloPagina",
                edicao ? "Revise os dados informados antes de salvar a alteracao."
                        : "Corrija os campos indicados para concluir o cadastro.");
    }
}