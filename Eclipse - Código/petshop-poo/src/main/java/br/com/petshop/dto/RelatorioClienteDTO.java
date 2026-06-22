package br.com.petshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.com.petshop.model.Animal;
import br.com.petshop.model.Atendimento;
import br.com.petshop.model.Proprietario;

/**
 * DTO responsavel por reunir os dados do relatorio personalizado por cliente.
 *
 * Esta classe consolida informacoes do tutor, seus animais, atendimentos
 * realizados no periodo informado e totais agrupados por servico e por data.
 * Dessa forma, a camada de apresentacao recebe uma estrutura unica e organizada
 * para exibicao do relatorio.
 */
public class RelatorioClienteDTO {

    private Proprietario proprietario;
    private List<Animal> animais;
    private List<Atendimento> atendimentos;
    private List<TotalPorServicoDTO> totaisPorServico;
    private List<TotalPorDataDTO> totaisPorData;
    private BigDecimal valorTotal;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public RelatorioClienteDTO(Proprietario proprietario, List<Animal> animais, List<Atendimento> atendimentos,
            List<TotalPorServicoDTO> totaisPorServico, List<TotalPorDataDTO> totaisPorData, BigDecimal valorTotal,
            LocalDate dataInicio, LocalDate dataFim) {
        this.proprietario = proprietario;
        this.animais = animais;
        this.atendimentos = atendimentos;
        this.totaisPorServico = totaisPorServico;
        this.totaisPorData = totaisPorData;
        this.valorTotal = valorTotal;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public List<Animal> getAnimais() {
        return animais;
    }

    public List<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public List<TotalPorServicoDTO> getTotaisPorServico() {
        return totaisPorServico;
    }

    public List<TotalPorDataDTO> getTotaisPorData() {
        return totaisPorData;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }
}