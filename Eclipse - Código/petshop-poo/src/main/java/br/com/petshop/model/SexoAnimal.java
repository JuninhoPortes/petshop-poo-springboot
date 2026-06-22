package br.com.petshop.model;

/**
 * Enumeracao responsavel por padronizar a informacao de sexo biologico
 * associada aos animais cadastrados.
 *
 * O uso de enum evita a gravacao de valores livres e inconsistentes no banco
 * de dados, contribuindo para maior integridade das informacoes e facilitando
 * filtros, exibicoes e validacoes nas telas do sistema.
 */
public enum SexoAnimal {

    MACHO("Macho"),
    FEMEA("Femea");

    private final String descricao;

    SexoAnimal(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}