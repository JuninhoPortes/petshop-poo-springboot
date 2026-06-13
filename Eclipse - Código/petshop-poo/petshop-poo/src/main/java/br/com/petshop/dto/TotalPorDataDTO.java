package br.com.petshop.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO responsavel por representar o total financeiro agrupado por data.
 *
 * Esta estrutura organiza os atendimentos realizados em cada dia do periodo
 * consultado, permitindo que o relatorio apresente tanto a quantidade de
 * servicos executados quanto o valor total obtido por data.
 */
public class TotalPorDataDTO {

    private LocalDate data;
    private Long quantidade;
    private BigDecimal valorTotal;

    public TotalPorDataDTO(LocalDate data, Long quantidade, BigDecimal valorTotal) {
        this.data = data;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    public LocalDate getData() {
        return data;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}