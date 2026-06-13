package br.com.petshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidade responsavel por representar os servicos oferecidos pelo pet shop.
 *
 * A classe Servico permite cadastrar procedimentos como banho, tosa, consulta
 * ou outros atendimentos pertinentes ao negocio. O preco-base registrado nesta
 * entidade pode ser utilizado como referencia no lancamento de atendimentos,
 * mantendo liberdade para eventuais ajustes no valor cobrado em cada caso.
 */
@Entity
@Table(name = "servicos")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do servico e obrigatorio.")
    @Size(max = 100, message = "O nome deve possuir no maximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 500, message = "A descricao deve possuir no maximo 500 caracteres.")
    @Column(length = 500)
    private String descricao;

    @NotNull(message = "O preco do servico e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O preco deve ser maior que zero.")
    @Column(name = "preco_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;

    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Atendimento> atendimentos = new ArrayList<>();

    public Servico() {
    }

    public Servico(String nome, String descricao, BigDecimal precoBase) {
        this.nome = nome;
        this.descricao = descricao;
        this.precoBase = precoBase;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPrecoBase() {
        return precoBase;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public List<Atendimento> getAtendimentos() {
        return atendimentos;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setAtendimentos(List<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }

    public void inativar() {
        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }
}