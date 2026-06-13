package br.com.petshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller responsavel pela pagina inicial da aplicacao.
 *
 * Esta classe integra a camada de controle da arquitetura MVC e atua como
 * ponto de entrada da navegacao web. Sua responsabilidade inicial e direcionar
 * o usuario para a tela principal do sistema, a partir da qual serao acessados
 * os modulos de tutores, animais, servicos, atendimentos e relatorios.
 *
 * A existencia deste controller permite validar, desde o inicio do projeto,
 * a comunicacao entre requisicao HTTP, camada de controle e template Thymeleaf.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}