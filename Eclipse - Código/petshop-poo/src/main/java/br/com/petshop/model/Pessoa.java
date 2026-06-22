package br.com.petshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe abstrata que representa dados comuns de pessoas vinculadas ao sistema.
 *
 * Esta classe foi definida como uma superclasse mapeada para concentrar
 * atributos compartilhados, como identificador e nome. No contexto do projeto,
 * ela demonstra a aplicacao de heranca na modelagem orientada a objetos,
 * permitindo que classes mais especificas reutilizem caracteristicas comuns
 * sem duplicacao de codigo.
 */
@MappedSuperclass
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome e obrigatorio.")
    @Size(max = 120, message = "O nome deve possuir no maximo 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String nome;

    public Pessoa() {
    }

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}