package com.example.bikesystem.item;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Truck {

    private String id;
    private List<Bike> bikeList;

    private int locationId;
    private int seq;
    private int xPosition;
    private int yPosition;


    public Truck(int seq) {
        this.id = UUID.randomUUID().toString();
        this.bikeList = new ArrayList<>();

        this.seq = seq;
        this.locationId = 0;
        this.xPosition = 0;
        this.yPosition = 0;
    }

    public int getLoadBikeCnt(){
        return bikeList.size();
    }
}
