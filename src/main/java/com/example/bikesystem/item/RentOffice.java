package com.example.bikesystem.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RentOffice {

    private String id;
    private int seq;
    private List<Bike> holdBikeList;

    private int xPosition;
    private int yPosition;


    public RentOffice() {
    }


    public RentOffice(List<Bike> bikes, int seq,int xPosition, int yPosition) {
        this.id = UUID.randomUUID().toString();
        holdBikeList = new ArrayList<>();
        this.seq = seq;

        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Bike rentBike(){
        return holdBikeList.get(0);
    }


    public boolean isSatisfiedByRent(){
        return holdBikeList.size()> 0 ?  true : false;
    }

    public String getId() {
        return id;
    }

    public int getSeq() {
        return seq;
    }

    public int getLocatedBikesCnt(){
        return holdBikeList.size();
    }
}
