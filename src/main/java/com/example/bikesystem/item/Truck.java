package com.example.bikesystem.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Truck {

    private String id;
    private List<Bike> bikeList;

    private int xPosition;
    private int yPosition;


    public Truck(int xPosition, int yPosition) {
        this.id = UUID.randomUUID().toString();
        this.bikeList = new ArrayList<>();
        this.xPosition = 0;
        this.yPosition = 0;
    }
}
