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
    public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository,
                                      GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
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


            List<String> salvoLocations1 = Arrays.asList("B5", "C5", "F1");
            List<String> salvoLocations2 = Arrays.asList("B4", "B5", "B6");
            List<String> salvoLocations3 = Arrays.asList("F2", "D5");
            List<String> salvoLocations4 = Arrays.asList("E1, H3, A2");
            List<String> salvoLocations5 = Arrays.asList("A2, A4, G6");
            List<String> salvoLocations6 = Arrays.asList("B5, D5, C7");
            List<String> salvoLocations7 = Arrays.asList("A3, H6");
            List<String> salvoLocations8 = Arrays.asList("C5, C6");


            Salvo salvo1 = new Salvo(1, salvoLocations1);
            Salvo salvo2 = new Salvo(1, salvoLocations2);
            Salvo salvo3 = new Salvo(2, salvoLocations3);
            Salvo salvo4 = new Salvo(2, salvoLocations4);
            Salvo salvo5 = new Salvo(1, salvoLocations5);
            Salvo salvo6 = new Salvo(1, salvoLocations6);
            Salvo salvo7 = new Salvo(2, salvoLocations7);
            Salvo salvo8 = new Salvo(2, salvoLocations8);
            gamePlayer1.addSalvo(salvo1);
            gamePlayer1.addSalvo(salvo3);
            gamePlayer1.addSalvo(salvo5);
            gamePlayer1.addSalvo(salvo7);
            gamePlayer2.addSalvo(salvo2);
            gamePlayer2.addSalvo(salvo4);
            gamePlayer2.addSalvo(salvo6);
            gamePlayer2.addSalvo(salvo8);




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

            salvoRepository.save(salvo1);
            salvoRepository.save(salvo2);
            salvoRepository.save(salvo3);
            salvoRepository.save(salvo4);
            salvoRepository.save(salvo5);
            salvoRepository.save(salvo6);
            salvoRepository.save(salvo7);
            salvoRepository.save(salvo8);
        };
    }
}