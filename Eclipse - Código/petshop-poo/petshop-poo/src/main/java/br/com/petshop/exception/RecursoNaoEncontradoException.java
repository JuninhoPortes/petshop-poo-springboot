package br.com.petshop.exception;

/**
 * Excecao utilizada quando um registro solicitado nao e localizado.
 *
 * Essa classe permite diferenciar falhas de busca de outros erros da aplicacao.
 * No contexto do sistema, sera utilizada quando entidades como tutor, animal,
 * servico ou atendimento nao forem encontradas no banco de dados.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}