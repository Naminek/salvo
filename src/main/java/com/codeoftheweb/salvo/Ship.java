package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String shipType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private  GamePlayer gamePlayer;

    @ElementCollection
    private Set<String> shipLocation = new HashSet<>();


    public Ship() {

    }

    public long getShipId() {
        return id;
    }

    public void setShipId(long id) {
        this.id = id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }


    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Set<String> getShipLocation() {
        return shipLocation;
    }

    public void setShipLocation(Set<String> shipLocation) {
        this.shipLocation = shipLocation;
    }
}
