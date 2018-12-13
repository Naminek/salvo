package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;


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
            put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> new LinkedHashMap<String, Object>() {{
                put("id", gamePlayer.getGamePlayerId());
                put("player", getPlayers(gamePlayer.getPlayer()));

            }}).collect(Collectors.toList()));
        }}).collect(Collectors.toList());
    }

    private Map<String, Object> getPlayers(Player player) {
        return new LinkedHashMap<String, Object>() {{
            put("id", player.getPlayerId());
            put("email", player.getEmail());
        }};
    }

    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public Map<String, Object> getGameView(@PathVariable("nn") Long gamePlayerId) {
        GamePlayer gamePlayer = gamePlayerRepo.findOne(gamePlayerId);

        return new LinkedHashMap<String, Object>() {{
            put("id", gamePlayer.getGame().getGameId());
            put("created", gamePlayer.getGame().getCreatedDate());
            put("gamePlayers", getGamePlayers(gamePlayer.getGame()));
            put("ships", getShips(gamePlayer));
        }};
    }

    private List<HashMap<String, Object>> getGamePlayers(Game game) {
        return game.getGamePlayers().stream().map(gamePlayer -> new LinkedHashMap<String, Object>() {{
            put("id", gamePlayer.getGamePlayerId());
            put("player", getPlayers(gamePlayer.getPlayer()));
        }}).collect(Collectors.toList());
    }

    private List<HashMap<String, Object>> getShips (GamePlayer gamePlayer) {
        return gamePlayer.getShips().stream().map(ship -> new LinkedHashMap<String, Object>() {{
            put("type", ship.getShipType());
            put("locations", ship.getlocations());
        }}).collect(Collectors.toList());
    }

//    private List<HashMap<String, Object>> getSalvoes (GamePlayer gamePlayer) {
//        return gamePlayer.getSalvos().stream().map(salvo -> new LinkedHashMap<String, Object>() {{
//            put()
//        }})
//    }
}
