package com.codeoftheweb.salvo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
//    private long id;
//    private long gameId;
//    private long playerId;
//    private Date date;
//
//
//    public GamePlayer() {
//    }
//
//    public GamePlayer(Player player, Game game, Game date) {
//        this.gameId = player.getPlayerId();
//        this.playerId = game.getGameId();
//        this.date = date.getCurrentDate();
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//
//
//
//    public long getGameId() {
//        return gameId;
//    }
//
//    public void setGameId(long gameId) {
//        this.gameId = gameId;
//    }
//
//    public long getPlayerId() {
//        return playerId;
//    }
//
//    public void setPlayerId(long playerId) {
//        this.playerId = playerId;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }


        private long id;
        private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game")
    private Game game;
//    @JoinColumn(name="date")
//    private Game date;

    public GamePlayer() {
    }

    public GamePlayer(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}



