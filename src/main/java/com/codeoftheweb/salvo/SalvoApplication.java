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
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
        return (args) -> {
            Player user1 = new Player("j.bauer@ctu.gov");
            Player user2 = new Player("c.obrian@ctu.gov");
            Player user3 = new Player("kim_bauer@gmail.com");
            Player user4 = new Player("t.almeida@ctu.gov");

            playerRepository.save(user1);
            playerRepository.save(user2);
            playerRepository.save(user3);
            playerRepository.save(user4);



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
            GamePlayer gamePlayer2 = new GamePlayer(date1);
            user1.addGamePlayer(gamePlayer1);
            game1.addGamePlayer(gamePlayer1);
            user2.addGamePlayer(gamePlayer2);
            game1.addGamePlayer(gamePlayer2);


            GamePlayer gamePlayer3 = new GamePlayer(date2);
            user1.addGamePlayer(gamePlayer3);
            game2.addGamePlayer(gamePlayer3);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);

            Ship ship1 = new Ship("Destroyer");
            Ship ship2 = new Ship("Submarine");
            Ship ship3 = new Ship("Patrol Boat");
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer1.addShip(ship3);



        };
    }
}