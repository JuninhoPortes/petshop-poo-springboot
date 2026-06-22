package br.com.petshop.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidade responsavel por representar o tutor ou proprietario de animais.
 *
 * A classe Proprietario especializa a abstracao Pessoa e acrescenta informacoes
 * de contato necessarias para o relacionamento comercial do pet shop com seus
 * clientes. Ela tambem estabelece a associacao com os animais cadastrados,
 * permitindo que o sistema consulte os pets pertencentes a um determinado
 * cliente e, posteriormente, gere relatorios por tutor e periodo.
 */
@Entity
@Table(name = "proprietarios")
public class Proprietario extends Pessoa {

    @NotBlank(message = "O endereco e obrigatorio.")
    @Size(max = 180, message = "O endereco deve possuir no maximo 180 caracteres.")
    @Column(nullable = false, length = 180)
    private String endereco;

    @NotBlank(message = "O telefone e obrigatorio.")
    @Size(max = 20, message = "O telefone deve possuir no maximo 20 caracteres.")
    @Column(nullable = false, length = 20)
    private String telefone;

    @Email(message = "Informe um e-mail valido.")
    @Size(max = 120, message = "O e-mail deve possuir no maximo 120 caracteres.")
    @Column(length = 120)
    private String email;

    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Animal> animais = new ArrayList<>();

    public Proprietario() {
    }

    public Proprietario(String nome, String endereco, String telefone, String email) {
        super(nome);
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public List<Animal> getAnimais() {
        return animais;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAnimais(List<Animal> animais) {
        this.animais = animais;
    }

    public void adicionarAnimal(Animal animal) {
        animais.add(animal);
        animal.setProprietario(this);
    }

    public void removerAnimal(Animal animal) {
        animais.remove(animal);
        animal.setProprietario(null);
    }
}