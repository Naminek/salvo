package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class SalvoController {

    private GameRepository gameRepo;
    private GamePlayerRepository gamePlayerRepo;
    private PlayerRepository playerRepo;
    private ShipRepository shipRepo;
    private SalvoRepository salvoRepo;
    private ScoreRepository scoreRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, PlayerRepository playerRepo, ShipRepository shipRepo, SalvoRepository salvoRepo, ScoreRepository scoreRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
        this.playerRepo = playerRepo;
        this.shipRepo = shipRepo;
        this.salvoRepo = salvoRepo;
        this.scoreRepo = scoreRepo;
    }


    @RequestMapping("/games")
    public Map<String, Object> getGamesForUser(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return new LinkedHashMap<String, Object>() {{
                put("player", "null");
                put("games", getGames());
            }};
        } else {
            return new LinkedHashMap<String, Object>() {{
                put("player", getPlayers(getViewingPlayer(authentication)));
                put("games", getGames());
            }};
        }
    }


    private Player getViewingPlayer(Authentication authentication) {
        return playerRepo.findByUserName(authentication.getName());
    }


    private List<Map<String, Object>> getGames() {
        return gameRepo
                .findAll()
                .stream()
                .map(game -> new LinkedHashMap<String, Object>() {{
                    put("id", game.getGameId());
                    put("created", game.getCreatedDate());
                    put("finished", checkFinished(game.getScores()));
                    put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> new LinkedHashMap<String, Object>() {{
                        put("gpid", gamePlayer.getGamePlayerId());
                        put("id", gamePlayer.getPlayer().getPlayerId());
                        put("name", gamePlayer.getPlayer().getEmail());
                        put("score", gamePlayer.getScoreInGame(game));
                    }}).collect(Collectors.toList()));
                }}).collect(Collectors.toList());
    }

    private Map<String, Object> getPlayers(Player player) {
        return new LinkedHashMap<String, Object>() {{
            put("playerId", player.getPlayerId());
            put("email", player.getEmail());
        }};
    }

    private Date checkFinished(Set<Score> score) {
        return score
                .stream()
                .map(sc -> sc.getFinishDate())
                .findFirst()
                .orElse(null);
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewGame(Authentication auth) {
        if (auth.getName().isEmpty()) {
            return new ResponseEntity<>(responseentity("error", "No user given"), HttpStatus.FORBIDDEN);
        } else {
            Date date = new Date();
            Game game = new Game(date);
            Player player = playerRepo.findByUserName(auth.getName());
            GamePlayer gamePlayer = new GamePlayer(date);
            gamePlayerRepo.save(gamePlayer);
            game.addGamePlayer(gamePlayer);
            player.addGamePlayer(gamePlayer);
            gameRepo.save(game);
            playerRepo.save(player);
            return new ResponseEntity<>(responseentity("success", "Game added"), HttpStatus.CREATED);
        }
    }

    private Map<String, Object> responseentity(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    @RequestMapping("/leaderboard")
    public List<Map<String, Object>> getGameTable() {
        return playerRepo
                .findAll()
                .stream()
                .map(player -> new LinkedHashMap<String, Object>() {{
                    put("playerEmail", player.getEmail());
                    put("scores", player.getScores()
                            .stream()
                            .map(score -> score.getScore()).collect(Collectors.toList()));
                }}).collect(Collectors.toList());
    }



    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public Object getGameView(@PathVariable("nn") Long gamePlayerId, Authentication auth) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);
        if (auth.getName() == gamePlayer.getPlayer().getEmail()) {
            return new LinkedHashMap<String, Object>() {{
                put("gameId", gamePlayer.getGame().getGameId());
                put("created", gamePlayer.getGame().getCreatedDate());
                put("gamePlayers", getGamePlayers(gamePlayer.getGame()));
                put("ships", getShips(gamePlayer));
                put("salvos", getSalvos(gamePlayer.getGame().getGamePlayers()));
                put("hitResults", getResults(gamePlayer));
                if(getOpponent(gamePlayer) != null) {
                    put("opponentPlayer", true);
                } else {
                    put("opponentPlayer", false);
                }
                if(getOpponent(gamePlayer) != null && getOpponent(gamePlayer).getShips().size() > 0) {
                    put("opponentShipsSet", true);
                } else {
                    put("opponentShipsSet", false);
                }
                put("lastTurn", getTurns(gamePlayer));
                put("winner", getWinner(gamePlayer));
            }};
        } else {
            return new ResponseEntity<>("Wrong user", HttpStatus.UNAUTHORIZED);
        }
    }

    private List<Map<String, Object>> getGamePlayers(Game game) {
        return game.getGamePlayers()
                .stream()
                .map(gamePlayer -> new LinkedHashMap<String, Object>() {{
                    put("gamePlayerId", gamePlayer.getGamePlayerId());
                    put("player", getPlayers(gamePlayer.getPlayer()));
                }}).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getShips(GamePlayer gamePlayer) {
        return gamePlayer.getShips()
                .stream()
                .map(ship -> new LinkedHashMap<String, Object>() {{
                    put("type", ship.getShipType());
                    put("locations", ship.getlocations());
                }}).collect(Collectors.toList());
    }

    private List<Map<String, Object>> getSalvos(Set<GamePlayer> gamePlayer) {
        return gamePlayer.stream()
                .flatMap(oneGamePlayer -> oneGamePlayer.getSalvos()
                        .stream().map(salvo -> new LinkedHashMap<String, Object>() {{
                            put("turn", salvo.getTurn());
                            put("gamePlayerId", salvo.getGamePlayer().getGamePlayerId());
                            put("locations", salvo.getSalvoLocations());
                        }})
                ).collect(Collectors.toList());
    }

    private GamePlayer getOpponent(GamePlayer gamePlayer) {
        Game game = gamePlayer.getGame();
        LinkedHashMap<String, GamePlayer> helperMap = new LinkedHashMap<>();
        if(game.getGamePlayers().size() < 2) {
            helperMap.put("opponent", null);
        } else {
        game.getGamePlayers().forEach(gp -> {
            if (gp.getGamePlayerId() != gamePlayer.getGamePlayerId()) {
                helperMap.put("opponent", gp);
            }
        });
        }
        return helperMap.get("opponent");
    }

    private List<Map<String, Object>> getHitResults(GamePlayer gamePlayer) {
        List<Salvo> salvoList = gamePlayer.getSalvos().stream().collect(Collectors.toList());
        Comparator<Salvo> compareSalvo = new Comparator<Salvo>() {
            @Override
            public int compare(Salvo o1, Salvo o2) {
                return o1.getTurn().compareTo(o2.getTurn());
            }
        };
        Collections.sort(salvoList, compareSalvo);
        Map<String, Integer> totalDamages = new LinkedHashMap<>();
        Map<String, Object> isSunk = new LinkedHashMap<>();
        totalDamages.put("aircraft carrier", 0);
        isSunk.put("aircraftIsSunk", false);
        totalDamages.put("battleship", 0);
        isSunk.put("battleshipIsSunk", false);
        totalDamages.put("destroyer", 0);
        isSunk.put("destroyerIsSunk", false);
        totalDamages.put("submarine", 0);
        isSunk.put("submarineIsSunk", false);
        totalDamages.put("patrol boat", 0);
        isSunk.put("patrolIsSunk", false);

        List<Map<String, Object>> hitList = salvoList.stream().map(salvo ->
                new LinkedHashMap<String, Object>() {{
                    put("turn", salvo.getTurn());
                    put("hits", getOneHitResult(salvo.getSalvoLocations(), gamePlayer, totalDamages));
                    put("totalDamage", totalDamages);
                        if (totalDamages.get("aircraft carrier") == 5) {
                            isSunk.replace("aircraftIsSunk", true);
                        }
                        if (totalDamages.get("battleship") == 4) {
                            isSunk.replace("battleshipIsSunk", true);
                        }
                        if (totalDamages.get("destroyer") == 3) {
                            isSunk.replace("destroyerIsSunk", true);
                        }
                        if (totalDamages.get("submarine") == 3) {
                            isSunk.replace("submarineIsSunk", true);
                        }
                        if (totalDamages.get("patrol boat") == 2) {
                            isSunk.replace("patrolIsSunk", true);
                        }
                        put("isSunk", isSunk);
                    if(isSunk.get("aircraftIsSunk").equals(true) && isSunk.get("battleshipIsSunk").equals(true)
                            && isSunk.get("destroyerIsSunk").equals(true) && isSunk.get("submarineIsSunk").equals(true)
                            && isSunk.get("patrolIsSunk").equals(true)) {
                        put("gameIsOver", true);
                    } else {
                        put("gameIsOver", false);

                    }
                }}
        ).collect(Collectors.toList());
        return hitList;
    }



    private List<Map<String, Object>> getOneHitResult(List<String> salvoLocations, GamePlayer gamePlayer, Map<String, Integer> totalDamages) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (getOpponent(gamePlayer).getShips() != null) {

            getOpponent(gamePlayer).getShips().stream().forEach(ship -> {
//                System.out.println("gpig" + getOpponent(gamePlayer).getGamePlayerId() + " ship " + ship.getShipType() + " / " + ship.getDamage());
                for (int i = 0; i < salvoLocations.size(); i++) {
                    if (ship.getlocations().contains(salvoLocations.get(i))) {
                        Map<String, Object> tempMap = new LinkedHashMap<>();
                        tempMap.put("hitShip", ship.getShipType());
                        tempMap.put("hitPlace", salvoLocations.get(i));

                        totalDamages.replace(ship.getShipType(), totalDamages.get(ship.getShipType()) + 1);

                        result.add(tempMap);
                    }
                }
            });
        }
        return result;
    }


    private List<Map<String, Object>> getResults(GamePlayer gamePlayer) {
        List<Map<String, Object>> resultData = new ArrayList<>();
        Map<String, Object> myMap = new LinkedHashMap<>();
        myMap.put("gamePlayerId", gamePlayer.getGamePlayerId());
        myMap.put("attack", getHitResults(gamePlayer));
        resultData.add(0, myMap);

        if (getOpponent(gamePlayer) != null) {
            Map<String, Object> opponentMap = new LinkedHashMap<>();
            opponentMap.put("gamePlayerId", getOpponent(gamePlayer).getGamePlayerId());
            opponentMap.put("attack", getHitResults(getOpponent(gamePlayer)));
            resultData.add(1, opponentMap);
        }
        return resultData;
    }

    private Integer checkLastTurn(GamePlayer gamePlayer) {
        if(gamePlayer.getSalvos().size() > 0) {
            List<Integer> turnList = gamePlayer.getSalvos().stream().map(salvo -> salvo.getTurn()).collect(Collectors.toList());
            Comparator<Integer> compareTurn = new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            };
            Collections.sort(turnList, compareTurn);
            return turnList.get(turnList.size() - 1);
        } else {
            return null;
        }
    }

    private Map<String, Integer> getTurns(GamePlayer gamePlayer) {
        Map<String, Integer> turns = new LinkedHashMap<>();
        if(gamePlayer.getSalvos().size() > 0) {
            turns.put("myLastTurn", checkLastTurn(gamePlayer));
        } else {
            turns.put("myLastTurn", null);
        }
        if(getOpponent(gamePlayer) != null && getOpponent(gamePlayer).getSalvos().size() > 0) {
            turns.put("opponentLastTurn", checkLastTurn(getOpponent(gamePlayer)));
        } else {
            turns.put("opponentLastTurn", null);
        }
        return turns;
    }

    private boolean checkIfGameIsOver(GamePlayer gamePlayer) {
        if(checkLastTurn(gamePlayer) != null && checkLastTurn(getOpponent(gamePlayer)) != null && checkLastTurn(gamePlayer) == checkLastTurn(getOpponent(gamePlayer))) {
            if(getHitResults(gamePlayer).get(getHitResults(gamePlayer).size() - 1).get("gameIsOver").equals(true)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private String getWinner(GamePlayer gamePlayer) {
        if(getOpponent(gamePlayer) != null) {
            Score score = new Score();
            if(checkIfGameIsOver(gamePlayer) && checkIfGameIsOver(getOpponent(gamePlayer))) {
                if(checkIfScoreAdded(gamePlayer)) {
                    score.setScore(0.5);
                    score.setFinishDate(new Date());
                    gamePlayer.getGame().addScore(score);
                    gamePlayer.getPlayer().addScore(score);
                    scoreRepo.save(score);
                }
                return "tied";
            } else if (checkIfGameIsOver(gamePlayer)) {
                if(checkIfScoreAdded(gamePlayer)) {
                    score.setScore(1.0);
                    score.setFinishDate(new Date());
                    gamePlayer.getGame().addScore(score);
                    gamePlayer.getPlayer().addScore(score);
                    scoreRepo.save(score);
                }
                return gamePlayer.getPlayer().getEmail();
            } else if (checkIfGameIsOver(getOpponent(gamePlayer))) {
                if(checkIfScoreAdded(gamePlayer)) {
                    score.setScore(0.0);
                    score.setFinishDate(new Date());
                    gamePlayer.getGame().addScore(score);
                    gamePlayer.getPlayer().addScore(score);
                    scoreRepo.save(score);
                }
                return getOpponent(gamePlayer).getPlayer().getEmail();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private boolean checkIfScoreAdded(GamePlayer gamePlayer) {
        if(gamePlayer.getGame().getScores().size() == 0) {
            return true;
        } else {
            List<Score> scoreList = gamePlayer.getPlayer().getScores().stream().filter(score -> score.getGame() == gamePlayer.getGame()).collect(Collectors.toList());
            if(scoreList.size() > 0) {
                return false;
            } else {
                return true;
            }
        }

    }



    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        Player player = playerRepo.findByUserName(email);
        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(responseentity("error", "No user given"), HttpStatus.FORBIDDEN);
        } else if (player != null) {
            return new ResponseEntity<>(responseentity("error", "Email in use"), HttpStatus.FORBIDDEN);
        } else {
            playerRepo.save(new Player(email, password));
            return new ResponseEntity<>(responseentity("success", "User added"), HttpStatus.CREATED);
        }
    }


    @RequestMapping(value = "/games/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(@PathVariable Long gameId, Authentication auth) {
        Game game = gameRepo.findOne(gameId);
        Player player = playerRepo.findByUserName(auth.getName());
        if (auth.getName().isEmpty()) {
            return new ResponseEntity<>(responseentity("error", "No user given"), HttpStatus.UNAUTHORIZED);
        } else if (game == null) {
            return new ResponseEntity<>(responseentity("error", "No such game"), HttpStatus.UNAUTHORIZED);
        } else if (game.getGamePlayers().size() > 1) {
            return new ResponseEntity<>(responseentity("error", "Game is full"), HttpStatus.FORBIDDEN);
        } else if (game.getPlayers().contains(player)) {
            return new ResponseEntity<>(responseentity("error", "User already joined"), HttpStatus.FORBIDDEN);
        } else {
            Date date = new Date();
            GamePlayer gamePlayer = new GamePlayer(date);
            gamePlayerRepo.save(gamePlayer);
            game.addGamePlayer(gamePlayer);
            player.addGamePlayer(gamePlayer);
            gameRepo.save(game);
            playerRepo.save(player);

            return new ResponseEntity<>(responseentity("success", "Player added"), HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getShips(@PathVariable Long gamePlayerId, Authentication auth, @RequestBody ArrayList<Ship> shipList) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);
        if (auth.getName().isEmpty()) {
            return new ResponseEntity<>(responseentity("error", "No user given"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer == null) {
            return new ResponseEntity<>(responseentity("error", "No such game player"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer.getPlayer().getEmail() != auth.getName()) {
            return new ResponseEntity<>(responseentity("error", "Not Correct player"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer.getShips().size() != 0) {
            return new ResponseEntity<>(responseentity("error", "Ships placed"), HttpStatus.FORBIDDEN);
        } else {
            shipList.forEach(ship -> {
                gamePlayer.addShip(ship);
                shipRepo.save(ship);
            });
//            gamePlayerRepo.save(gamePlayer); i don't have to save but update the information of gp because he is already existing
            return new ResponseEntity<>(responseentity("success", "Ship added"), HttpStatus.CREATED);

        }
    }

    @RequestMapping(value = "/games/players/{gamePlayerId}/salvos", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getSalvos(@PathVariable Long gamePlayerId, Authentication auth,
                                                         @RequestBody Salvo salvoLocations) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);
        if (auth.getName().isEmpty()) {
            return new ResponseEntity<>(responseentity("error", "No user given"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer == null) {
            return new ResponseEntity<>(responseentity("error", "No such game player"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer.getPlayer().getEmail() != auth.getName()) {
            return new ResponseEntity<>(responseentity("error", "Not Correct player"), HttpStatus.UNAUTHORIZED);
        } else if (gamePlayer.getSalvos().size() != 0 && checkSalvoTurn(gamePlayer, salvoLocations)) {
            return new ResponseEntity<>(responseentity("error", "salvos are already placed in this turn"), HttpStatus.FORBIDDEN);
        } else if ((checkLastTurn(gamePlayer) != null && checkLastTurn(getOpponent(gamePlayer)) != null && checkLastTurn(gamePlayer) > checkLastTurn(getOpponent(gamePlayer)))
                || checkLastTurn(gamePlayer) != null && checkLastTurn(getOpponent(gamePlayer)) == null) {
            return new ResponseEntity<>(responseentity("error", "Opponent player's turn"), HttpStatus.NOT_ACCEPTABLE);
        } else if(checkIfGameIsOver(gamePlayer) || checkIfGameIsOver(getOpponent(gamePlayer))) {
            return new ResponseEntity<>(responseentity("error", "Game finished"), HttpStatus.NOT_ACCEPTABLE);
        } else {
            gamePlayer.addSalvo(salvoLocations);
            salvoRepo.save(salvoLocations);
            return new ResponseEntity<>(responseentity("success", "Salvo added"), HttpStatus.CREATED);
        }
    }

    private boolean checkSalvoTurn(GamePlayer gamePlayer, Salvo salvoLocations) {
        List<Integer> myTurnList = gamePlayer.getSalvos().stream().map(salvo -> salvo.getTurn()).collect(Collectors.toList());
        if(myTurnList.contains(salvoLocations.getTurn())) {
            return true;
        } else {
            return false;
        }
    }



}


