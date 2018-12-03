package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository) {
        return (args) -> {
            playerRepository.save(new Player("aaa@aaaaa"));
            playerRepository.save(new Player("bbb@bbbbb"));
            playerRepository.save(new Player("ccc@ccccc"));
            playerRepository.save(new Player("ddd@ddddd"));
            playerRepository.save(new Player("eee@eeeee"));
        };
    }

}
