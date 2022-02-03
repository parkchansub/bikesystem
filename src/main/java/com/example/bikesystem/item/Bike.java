package com.example.bikesystem.item;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Bike {

    private String id;
    private Integer returnRentOfficeId;
    private Integer retrunTime;

    public Bike() {
        this.id = UUID.randomUUID().toString();
    }

    public Bike rentBike(Integer retrunTime, Integer returnRentOfficeId) {
        this.retrunTime = retrunTime;
        this.returnRentOfficeId = returnRentOfficeId;
        return this;
    }

    public Integer getRetrunTime() {
        return retrunTime;
    }
}
