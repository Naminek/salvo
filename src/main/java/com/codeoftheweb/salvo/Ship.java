package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

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
//    private Set<String> shipLocations = new HashSet<>();
    private List<String> locations = new ArrayList<>();

    public Ship() {

    }

    public Ship(String shipType, List<String> locations) {
        this.shipType = shipType;
        this.locations = locations;
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

    public List<String> getlocations() {
        return locations;
    }

    public void setlocation(List<String> locations) {
        this.locations = locations;
    }
}
