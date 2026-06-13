package br.com.petshop.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.petshop.dto.RelatorioClienteDTO;
import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.service.ProprietarioService;
import br.com.petshop.service.RelatorioClienteService;

/**
 * Controller responsavel pelo modulo de relatorios personalizados.
 *
 * Esta classe disponibiliza a consulta de relatorio por tutor e periodo,
 * reunindo dados cadastrais do cliente, animais vinculados, servicos prestados
 * e totais consolidados. A finalidade e apresentar uma visao gerencial dos
 * atendimentos realizados pelo pet shop.
 */
@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioClienteService relatorioClienteService;
    private final ProprietarioService proprietarioService;

    public RelatorioController(RelatorioClienteService relatorioClienteService, ProprietarioService proprietarioService) {
        this.relatorioClienteService = relatorioClienteService;
        this.proprietarioService = proprietarioService;
    }

    @GetMapping
    public String relatorioCliente(
            @RequestParam(name = "gerar", required = false, defaultValue = "false") boolean gerar,
            @RequestParam(name = "proprietarioId", required = false) Long proprietarioId,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        model.addAttribute("proprietarios", proprietarioService.listarTodos());
        model.addAttribute("consultaRealizada", gerar);
        model.addAttribute("proprietarioId", proprietarioId);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        if (gerar) {
            try {
                RelatorioClienteDTO relatorio = relatorioClienteService.gerarRelatorio(
                        proprietarioId, dataInicio, dataFim);

                model.addAttribute("relatorio", relatorio);
            } catch (RegraNegocioException | RecursoNaoEncontradoException exception) {
                model.addAttribute("mensagemErro", exception.getMessage());
                model.addAttribute("consultaRealizada", false);
            }
        }

        return "relatorios/cliente";
    }
}