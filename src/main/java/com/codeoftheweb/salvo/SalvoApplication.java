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
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
        return (args) -> {
            Player user1 = new Player("aaa@aaaaa");
            Player user2 = new Player("bbb@bbbbb");
            Player user3 = new Player("ccc@ccccc");
            Player user4 = new Player("ddd@ddddd");
            Player user5 = new Player("eee@eeeee");

            playerRepository.save(user1);
            playerRepository.save(user2);
            playerRepository.save(user3);
            playerRepository.save(user4);
            playerRepository.save(user5);


            Date date = new Date();
            Date date1 = date.from(date.toInstant().plusSeconds(3600));
            Date date2 = date.from(date.toInstant().plusSeconds(7200));

            gameRepository.save(new Game(date));
            gameRepository.save(new Game(date1));
            gameRepository.save(new Game(date2));

//            gamePlayerRepository.save(new GamePlayer(user1, new Game(date), date));



        };
    }
}