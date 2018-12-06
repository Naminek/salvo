package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;


@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepo;


    @RequestMapping("/games")
    public List<Map<String, Object>> getGames() {
        return gameRepo.findAll().stream().map(game -> new HashMap<String, Object>() {{
            put("gameId", game.getGameId());
            put("created", game.getCreatedDate());
        }}).collect(Collectors.toList());
    }


}
