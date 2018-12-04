package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private long gameId;
    private long playerId;
    private Date date;

    public GamePlayer(Player user1, Game game, Date date) {
    }

    public GamePlayer(long game, long player, Date currentDate) {
        gameId = game;
        playerId = player;
        date = currentDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
