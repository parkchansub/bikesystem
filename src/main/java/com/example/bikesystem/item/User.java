package com.example.bikesystem.item;

import java.util.UUID;

public class User {

    private String id;
    private Bike rentBike;


    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public void rentBike(Bike bike) {
        this.rentBike = bike;
    }

    public void returnBike(){
        rentBike = null;
    }


}
