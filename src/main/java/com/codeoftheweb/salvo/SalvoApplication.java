package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


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



            Date date1 = new Date();
            Date date2 = date1.from(date1.toInstant().plusSeconds(3600));
            Date date3 = date1.from(date1.toInstant().plusSeconds(7200));

            Game game1 = new Game(date1);
            Game game2 = new Game(date2);
            Game game3 = new Game(date3);

            GamePlayer gamePlayer1 = new GamePlayer(date1);
            GamePlayer gamePlayer2 = new GamePlayer(date1);
            user1.addGamePlayer(gamePlayer1);
            game1.addGamePlayer(gamePlayer1);
            user2.addGamePlayer(gamePlayer2);
            game1.addGamePlayer(gamePlayer2);
            GamePlayer gamePlayer3 = new GamePlayer(date2);
            user1.addGamePlayer(gamePlayer3);
            game2.addGamePlayer(gamePlayer3);


            List<String> locations1 = Arrays.asList("H2", "H3", "H4");
            List<String> locations2 = Arrays.asList("E1", "F1", "G1");
            List<String> locations3 = Arrays.asList("B4", "B5");
            List<String> locations4 = Arrays.asList("B5", "C5", "D5");
            List<String> locations5 = Arrays.asList("C6", "C7");
            List<String> locations6 = Arrays.asList("A2", "A3", "A4");

            Ship ship1 = new Ship("Destroyer", locations1);
            Ship ship2 = new Ship("Submarine", locations2);
            Ship ship3 = new Ship("Patrol Boat", locations3);
            Ship ship4 = new Ship("Destroyer", locations4);
            Ship ship5 = new Ship("Submarine", locations5);
            Ship ship6 = new Ship("Patrol Boat", locations6);
            gamePlayer1.addShip(ship1);
            gamePlayer1.addShip(ship2);
            gamePlayer1.addShip(ship3);
            gamePlayer2.addShip(ship4);
            gamePlayer2.addShip(ship5);
            gamePlayer2.addShip(ship6);


            playerRepository.save(user1);
            playerRepository.save(user2);
            playerRepository.save(user3);
            playerRepository.save(user4);

            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);

            gamePlayerRepository.save(gamePlayer1);
            gamePlayerRepository.save(gamePlayer2);
            gamePlayerRepository.save(gamePlayer3);




            shipRepository.save(ship1);
            shipRepository.save(ship2);
            shipRepository.save(ship3);
            shipRepository.save(ship4);
            shipRepository.save(ship5);
            shipRepository.save(ship6);


        };
    }
}