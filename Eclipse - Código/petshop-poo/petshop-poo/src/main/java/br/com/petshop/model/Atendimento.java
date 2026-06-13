package br.com.petshop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidade que registra a execucao de um servico para um animal cadastrado.
 *
 * O Atendimento materializa o vinculo entre animal, servico, data, valor
 * cobrado e observacoes do procedimento realizado. Essa estrutura sera a base
 * para a consulta de historico por animal e para a geracao de relatorios
 * personalizados por cliente, periodo, servico e data.
 */
@Entity
@Table(name = "atendimentos")
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data do atendimento e obrigatoria.")
    @Column(name = "data_atendimento", nullable = false)
    private LocalDate dataAtendimento;

    @NotNull(message = "O animal atendido e obrigatorio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @NotNull(message = "O servico executado e obrigatorio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @NotNull(message = "O valor cobrado e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O valor cobrado deve ser maior que zero.")
    @Column(name = "valor_cobrado", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCobrado;

    @Size(max = 500, message = "As observacoes devem possuir no maximo 500 caracteres.")
    @Column(length = 500)
    private String observacoes;

    public Atendimento() {
    }

    public Atendimento(LocalDate dataAtendimento, Animal animal, Servico servico, BigDecimal valorCobrado,
            String observacoes) {
        this.dataAtendimento = dataAtendimento;
        this.animal = animal;
        this.servico = servico;
        this.valorCobrado = valorCobrado;
        this.observacoes = observacoes;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataAtendimento() {
        return dataAtendimento;
    }

    public Animal getAnimal() {
        return animal;
    }

    public Servico getServico() {
        return servico;
    }

    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataAtendimento(LocalDate dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public void setValorCobrado(BigDecimal valorCobrado) {
        this.valorCobrado = valorCobrado;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}