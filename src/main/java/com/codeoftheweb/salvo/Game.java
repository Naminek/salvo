package com.codeoftheweb.salvo;


import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date currentDate;

    public Game() {
    }

    public Game(Date date) {
        currentDate = date;
    }

    public long getId() {
        return id;
    }

    public Date getCurrentDate(){
        return currentDate;
    }


    public void setId(long id) {
        this.id = id;
    }

    public void setCurrentDate(Date date) {
        this.currentDate = date;
    }
}

