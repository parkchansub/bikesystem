package com.example.bikesystem.item;

import lombok.Getter;

import java.util.UUID;

@Getter
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
