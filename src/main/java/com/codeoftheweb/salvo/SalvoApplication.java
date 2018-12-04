package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;


@SpringBootApplication
public class SalvoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SalvoApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository) {
        return (args) -> {
            playerRepository.save(new Player("aaa@aaaaa"));
            playerRepository.save(new Player("bbb@bbbbb"));
            playerRepository.save(new Player("ccc@ccccc"));
            playerRepository.save(new Player("ddd@ddddd"));
            playerRepository.save(new Player("eee@eeeee"));


            Date date = new Date();
            Date date1 = date.from(date.toInstant().plusSeconds(3600));
            Date date2 = date.from(date.toInstant().plusSeconds(7200));

            gameRepository.save(new Game(date));
            gameRepository.save(new Game(date1));
            gameRepository.save(new Game(date2));
        };
    }
}