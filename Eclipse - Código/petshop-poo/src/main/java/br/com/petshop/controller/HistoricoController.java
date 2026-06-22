package br.com.petshop.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.model.Atendimento;
import br.com.petshop.service.AnimalService;
import br.com.petshop.service.AtendimentoService;
import br.com.petshop.service.ServicoService;

/**
 * Controller responsavel pela consulta de historico de servicos por animal.
 *
 * Esta classe disponibiliza filtros por animal, tipo de servico e periodo,
 * permitindo que o usuario acompanhe os atendimentos realizados ao longo do
 * tempo. A consulta somente e executada apos uma acao explicita do usuario,
 * evitando que a tela apresente resultados antes da definicao dos filtros.
 */
@Controller
@RequestMapping("/historico")
public class HistoricoController {

    private final AtendimentoService atendimentoService;
    private final AnimalService animalService;
    private final ServicoService servicoService;

    public HistoricoController(AtendimentoService atendimentoService, AnimalService animalService,
            ServicoService servicoService) {
        this.atendimentoService = atendimentoService;
        this.animalService = animalService;
        this.servicoService = servicoService;
    }

    @GetMapping
    public String consultar(
            @RequestParam(name = "consultar", required = false, defaultValue = "false") boolean consultar,
            @RequestParam(name = "animalId", required = false) Long animalId,
            @RequestParam(name = "servicoId", required = false) Long servicoId,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        List<Atendimento> atendimentos = Collections.emptyList();

        if (consultar) {
            try {
                atendimentos = atendimentoService.filtrarHistorico(animalId, servicoId, dataInicio, dataFim);
            } catch (RegraNegocioException exception) {
                model.addAttribute("mensagemErro", exception.getMessage());
            }
        }

        model.addAttribute("atendimentos", atendimentos);
        model.addAttribute("animais", animalService.listarTodos());
        model.addAttribute("servicos", servicoService.listarTodos());

        model.addAttribute("consultaRealizada", consultar);
        model.addAttribute("animalId", animalId);
        model.addAttribute("servicoId", servicoId);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        model.addAttribute("quantidadeAtendimentos", atendimentos.size());
        model.addAttribute("valorTotal", calcularValorTotal(atendimentos));

        return "historico/consulta";
    }

    private BigDecimal calcularValorTotal(List<Atendimento> atendimentos) {
        return atendimentos.stream()
                .map(Atendimento::getValorCobrado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}