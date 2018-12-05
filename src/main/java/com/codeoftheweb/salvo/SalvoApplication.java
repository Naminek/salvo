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


            Date date1 = new Date();
            Date date2 = date1.from(date1.toInstant().plusSeconds(3600));
            Date date3 = date1.from(date1.toInstant().plusSeconds(7200));

            Game game1 = new Game(date1);
            Game game2 = new Game(date2);
            Game game3 = new Game(date3);

            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            GamePlayer gamePlayer1 = new GamePlayer(date1);
            user1.addGamePlayer(gamePlayer1);
            game1.addGamePlayer(gamePlayer1);

            GamePlayer gamePlayer2 = new GamePlayer(date2);
            user3.addGamePlayer(gamePlayer2);
            game1.addGamePlayer(gamePlayer2);

            game1.getPlayers();

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);



        };
    }
}