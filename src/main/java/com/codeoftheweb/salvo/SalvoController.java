package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api")
public class SalvoController {

    private GameRepository gameRepo;
    private GamePlayerRepository gamePlayerRepo;

    @Autowired
    public SalvoController(GameRepository gameRepo, GamePlayerRepository gamePlayerRepo) {
        this.gameRepo = gameRepo;
        this.gamePlayerRepo = gamePlayerRepo;
    }


    @RequestMapping("/games")
    public List<Map<String, Object>> getGames() {
        return gameRepo.findAll().stream().map(game -> new LinkedHashMap<String, Object>() {{
            put("id", game.getGameId());
            put("created", game.getCreatedDate());
            put("finished", checkFinished(game));
            put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> new LinkedHashMap<String, Object>() {{
                put("id", gamePlayer.getGamePlayerId());
                put("player", getPlayers(gamePlayer.getPlayer()));
//                put("score", getScores(gamePlayer.getGame()))
            }}).collect(Collectors.toList()));
        }}).collect(Collectors.toList());
    }

    private Map<String, Object> getPlayers(Player player) {
        return new LinkedHashMap<String, Object>() {{
            put("playerId", player.getPlayerId());
            put("email", player.getEmail());
        }};
    }

    private String checkFinished(Game game) {
        if (game.getScores().size() < 1) {
            return null;
        } else {
            return "finished";
        }
    }



    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public Map<String, Object> getGameView(@PathVariable("nn") Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);

        return new LinkedHashMap<String, Object>() {{
            put("gameId", gamePlayer.getGame().getGameId());
            put("created", gamePlayer.getGame().getCreatedDate());
            put("gamePlayers", getGamePlayers(gamePlayer.getGame()));
            put("ships", getShips(gamePlayer));
            put("salvoes", getSalvoes(gamePlayer.getGame().getGamePlayers()));
        }};
    }

    private List<HashMap<String, Object>> getGamePlayers(Game game) {
        return game.getGamePlayers()
                .stream().map(gamePlayer -> new LinkedHashMap<String, Object>() {{
                    put("GamePlayerId", gamePlayer.getGamePlayerId());
                    put("player", getPlayers(gamePlayer.getPlayer()));
                }}).collect(Collectors.toList());
    }

    private List<HashMap<String, Object>> getShips(GamePlayer gamePlayer) {
        return gamePlayer.getShips()
                .stream().map(ship -> new LinkedHashMap<String, Object>() {{
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

}
