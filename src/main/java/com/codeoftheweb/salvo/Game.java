package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.Date;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date date;

//    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
//    Set<GamePlayer> gamePlayer;

    public Game() {
    }

    public Game(Date currentDate) {
        date = currentDate;
    }

    public long getGameId() {
        return id;
    }

    public Date getCurrentDate(){
        return date;
    }


    public void setGameId(long id) {
        this.id = id;
    }

    public void setCurrentDate(Date currentDate) {
        this.date = currentDate;
    }
}

