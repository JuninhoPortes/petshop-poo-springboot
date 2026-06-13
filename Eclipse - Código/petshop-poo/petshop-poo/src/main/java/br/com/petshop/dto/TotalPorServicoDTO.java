package br.com.petshop.dto;

import java.math.BigDecimal;

/**
 * DTO responsavel por representar o total financeiro agrupado por servico.
 *
 * Esta estrutura e utilizada nos relatorios para apresentar, de forma
 * consolidada, a quantidade de atendimentos e o valor total gerado por cada
 * tipo de servico prestado em determinado periodo.
 */
public class TotalPorServicoDTO {

    private String nomeServico;
    private Long quantidade;
    private BigDecimal valorTotal;

    public TotalPorServicoDTO(String nomeServico, Long quantidade, BigDecimal valorTotal) {
        this.nomeServico = nomeServico;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}