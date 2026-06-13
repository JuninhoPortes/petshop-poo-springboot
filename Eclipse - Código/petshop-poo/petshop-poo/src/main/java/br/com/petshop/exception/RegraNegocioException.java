package br.com.petshop.exception;

/**
 * Excecao utilizada para representar violacoes de regra de negocio.
 *
 * Essa classe sera acionada quando uma operacao for tecnicamente possivel, mas
 * inadequada para as regras do sistema, como excluir um tutor que ainda possui
 * animais cadastrados ou registrar atendimento para um servico inativo.
 */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}