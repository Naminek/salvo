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
//    public Map<List<Long>, Integer> getGames() {
//        return gameRepo.findAll().stream().map(game -> game.getGameId()).collect(Collectors.toList());
//    }
    public List<Long> getGames() {
        return gameRepo.findAll().stream().map(game -> game.getGameId()).collect(Collectors.toList());
    }


}
