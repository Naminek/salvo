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

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo, PlayerRepository playerRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
        this.playerRepo = playerRepo;
    }


    @RequestMapping("/games")
    public Map<String, Object> getGamesForUser(Authentication authentication) {
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
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

//    private List<Map<String, Object>> getGames() {
//        return gameRepo
//                .findAll()
//                .stream()
//                .map(game -> new LinkedHashMap<String, Object>() {{
//                    put("id", game.getGameId());
//                    put("created", game.getCreatedDate());
//                    put("finished", checkFinished(game.getScores()));
//                    put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> new LinkedHashMap<String, Object>() {{
//                        put("id", gamePlayer.getGamePlayerId());
//                        put("player", getPlayers(gamePlayer.getPlayer()));
//                        put("score", gamePlayer.getScoreInGame(game));
//                    }}).collect(Collectors.toList()));
//                }}).collect(Collectors.toList());
//    }

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
    public ResponseEntity<Map<String, Object>> createNewGame(Authentication auth){
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
        System.out.println(gamePlayerId);
        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);
        System.out.println(gamePlayer);
        if(auth.getName() == gamePlayer.getPlayer().getEmail()) {
            return new LinkedHashMap<String, Object>() {{
                put("gameId", gamePlayer.getGame().getGameId());
                put("created", gamePlayer.getGame().getCreatedDate());
                put("gamePlayers", getGamePlayers(gamePlayer.getGame()));
                put("ships", getShips(gamePlayer));
                put("salvoes", getSalvoes(gamePlayer.getGame().getGamePlayers()));
            }};
        } else {
            return new ResponseEntity<>("Wrong user", HttpStatus.UNAUTHORIZED);
        }
    }

    private List<HashMap<String, Object>> getGamePlayers(Game game) {
        return game.getGamePlayers()
                .stream()
                .map(gamePlayer -> new LinkedHashMap<String, Object>() {{
                    put("gamePlayerId", gamePlayer.getGamePlayerId());
                    put("player", getPlayers(gamePlayer.getPlayer()));
                }}).collect(Collectors.toList());
    }

    private List<HashMap<String, Object>> getShips(GamePlayer gamePlayer) {
        return gamePlayer.getShips()
                .stream()
                .map(ship -> new LinkedHashMap<String, Object>() {{
                    put("type", ship.getShipType());
                    put("locations", ship.getlocations());
                }}).collect(Collectors.toList());
    }

    private List<HashMap<String, Object>> getSalvoes(Set<GamePlayer> gamePlayer) {
        return gamePlayer.stream()
                .flatMap(oneGamePlayer -> oneGamePlayer.getSalvos()
                        .stream().map(salvo -> new LinkedHashMap<String, Object>() {{
                            put("turn", salvo.getTurn());
                            put("gamePlayerId", salvo.getGamePlayer().getGamePlayerId());
                            put("locations", salvo.getSalvoLocations());
                        }})
                ).collect(Collectors.toList());
    }

    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam("email") String email, @RequestParam("password") String password){
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
        System.out.println(gameId);
        Game game = gameRepo.findOne(gameId);
        Player player = playerRepo.findByUserName(auth.getName());
        System.out.println(game);
        if(auth.getName().isEmpty()) {
            System.out.println("no auth");
            return new ResponseEntity<>(responseentity("error", "No user given"), HttpStatus.UNAUTHORIZED);
        } else if(game == null) {
            System.out.println("no game");
            return new ResponseEntity<>(responseentity("error", "No such game"), HttpStatus.FORBIDDEN);
        } else if(game.getGamePlayers().size() > 1) {
            System.out.println("game full");
            return new ResponseEntity<>(responseentity("error", "Game is full"), HttpStatus.FORBIDDEN);
        } else if(game.getPlayers().contains(player)) {
            System.out.println("already joined");
            return new ResponseEntity<>(responseentity("error", "User already joined"), HttpStatus.FORBIDDEN);
        } else {
            System.out.println("joining");
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


}


