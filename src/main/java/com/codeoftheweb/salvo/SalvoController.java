package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        return gameRepo.findAll().stream().map(game -> new HashMap<String, Object>() {{
            put("id", game.getGameId());
            put("created", game.getCreatedDate());
            put("gamePlayers", game.getGamePlayers().stream().map(gamePlayer -> new HashMap<String, Object>() {{
                put("id", gamePlayer.getGamePlayerId());
                put("player", createPlayerMap(gamePlayer.getPlayer()));

            }}).collect(Collectors.toList()));
        }}).collect(Collectors.toList());
    }

    private Map<String, Object> createPlayerMap(Player player) {
        return new HashMap<String, Object>() {{
            put("id", player.getPlayerId());
            put("email", player.getEmail());
        }};
    }

    @RequestMapping(value = "/game_view/{nn}", method = RequestMethod.GET)
    public Map<String, Object> getGameView(@PathVariable("nn") Long gamePlayerId) {

        return new HashMap<String, Object>() {{
            put("id", gamePlayerRepo.findOne(gamePlayerId).getGame().getGameId());
        }};
    }
}
