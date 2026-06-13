package br.com.petshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicacao PetShop POO.
 *
 * Esta classe representa o ponto de inicializacao do sistema desenvolvido para
 * a disciplina de Programacao Orientada a Objetos. A partir dela, o Spring Boot
 * carrega o contexto da aplicacao, identifica os componentes configurados nos
 * pacotes internos e disponibiliza a interface web do sistema.
 *
 * A escolha pelo Spring Boot permite estruturar a solucao em camadas, mantendo
 * a separacao entre controle, regras de negocio, persistencia e apresentacao,
 * conforme os principios da arquitetura MVC.
 */
@SpringBootApplication
public class PetshopPooApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetshopPooApplication.class, args);
    }

}