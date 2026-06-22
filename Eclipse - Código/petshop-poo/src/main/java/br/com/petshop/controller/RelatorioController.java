package br.com.petshop.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.petshop.dto.RelatorioClienteDTO;
import br.com.petshop.exception.RecursoNaoEncontradoException;
import br.com.petshop.exception.RegraNegocioException;
import br.com.petshop.service.ProprietarioService;
import br.com.petshop.service.RelatorioClientePdfService;
import br.com.petshop.service.RelatorioClienteService;

/**
 * Controller responsavel pelo modulo de relatorios personalizados.
 *
 * Esta classe disponibiliza a consulta de relatorio por tutor e periodo,
 * reunindo dados cadastrais do cliente, animais vinculados, servicos prestados
 * e totais consolidados. Alem da exibicao em tela, tambem permite a exportacao
 * do relatorio em PDF.
 */
@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioClienteService relatorioClienteService;
    private final RelatorioClientePdfService relatorioClientePdfService;
    private final ProprietarioService proprietarioService;

    public RelatorioController(RelatorioClienteService relatorioClienteService,
            RelatorioClientePdfService relatorioClientePdfService, ProprietarioService proprietarioService) {
        this.relatorioClienteService = relatorioClienteService;
        this.relatorioClientePdfService = relatorioClientePdfService;
        this.proprietarioService = proprietarioService;
    }

    @GetMapping
    public String relatorioCliente(
            @RequestParam(name = "gerar", required = false, defaultValue = "false") boolean gerar,
            @RequestParam(name = "proprietarioId", required = false) Long proprietarioId,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        prepararModeloBase(model, gerar, proprietarioId, dataInicio, dataFim);

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

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportarPdf(
            @RequestParam(name = "proprietarioId") Long proprietarioId,
            @RequestParam(name = "dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(name = "dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        RelatorioClienteDTO relatorio = relatorioClienteService.gerarRelatorio(proprietarioId, dataInicio, dataFim);
        byte[] pdf = relatorioClientePdfService.gerarPdf(relatorio);

        String nomeArquivo = "relatorio-cliente-" + proprietarioId + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(nomeArquivo).build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }

    private void prepararModeloBase(Model model, boolean gerar, Long proprietarioId, LocalDate dataInicio,
            LocalDate dataFim) {
        model.addAttribute("proprietarios", proprietarioService.listarTodos());
        model.addAttribute("consultaRealizada", gerar);
        model.addAttribute("proprietarioId", proprietarioId);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
    }
}