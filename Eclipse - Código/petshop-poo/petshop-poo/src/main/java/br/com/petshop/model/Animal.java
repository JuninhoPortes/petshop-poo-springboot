package br.com.petshop.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidade que representa um animal atendido pelo pet shop.
 *
 * Esta classe concentra os dados cadastrais do pet, como nome, especie, raca,
 * idade, sexo, peso e foto. A entidade tambem possui relacionamento com o
 * proprietario responsavel e com os atendimentos registrados ao longo do tempo,
 * permitindo a consulta de historico de servicos por animal, conforme previsto
 * nos requisitos do projeto.
 */
@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do animal e obrigatorio.")
    @Size(max = 80, message = "O nome deve possuir no maximo 80 caracteres.")
    @Column(nullable = false, length = 80)
    private String nome;

    @NotBlank(message = "A especie e obrigatoria.")
    @Size(max = 60, message = "A especie deve possuir no maximo 60 caracteres.")
    @Column(nullable = false, length = 60)
    private String especie;

    @Size(max = 80, message = "A raca deve possuir no maximo 80 caracteres.")
    @Column(length = 80)
    private String raca;

    @Min(value = 0, message = "A idade nao pode ser negativa.")
    @Column(nullable = false)
    private Integer idade;

    @NotNull(message = "O sexo do animal e obrigatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SexoAnimal sexo;

    @NotNull(message = "O peso do animal e obrigatorio.")
    @DecimalMin(value = "0.01", message = "O peso deve ser maior que zero.")
    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal peso;

    @Size(max = 255, message = "O caminho da foto deve possuir no maximo 255 caracteres.")
    @Column(name = "foto_caminho")
    private String fotoCaminho;

    @NotNull(message = "O proprietario do animal e obrigatorio.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Atendimento> atendimentos = new ArrayList<>();

    public Animal() {
    }

    public Animal(String nome, String especie, String raca, Integer idade, SexoAnimal sexo, BigDecimal peso,
            String fotoCaminho, Proprietario proprietario) {
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.idade = idade;
        this.sexo = sexo;
        this.peso = peso;
        this.fotoCaminho = fotoCaminho;
        this.proprietario = proprietario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaca() {
        return raca;
    }

    public Integer getIdade() {
        return idade;
    }

    public SexoAnimal getSexo() {
        return sexo;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public String getFotoCaminho() {
        return fotoCaminho;
    }

    public Proprietario getProprietario() {
        return proprietario;
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

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public void setSexo(SexoAnimal sexo) {
        this.sexo = sexo;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public void setFotoCaminho(String fotoCaminho) {
        this.fotoCaminho = fotoCaminho;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public void setAtendimentos(List<Atendimento> atendimentos) {
        this.atendimentos = atendimentos;
    }

    public void registrarAtendimento(Atendimento atendimento) {
        atendimentos.add(atendimento);
        atendimento.setAnimal(this);
    }
}