package com.example.bikesystem.item;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Bike {

    private String id;
    private Integer rentRentOfficeId;
    private Integer returnRentOfficeId;
    private Integer retrunTime;

    public Bike() {
        this.id = UUID.randomUUID().toString();
    }

    @Builder
    public Bike(Integer rentRentOfficeId, Integer returnRentOfficeId, Integer retrunTime) {
        this.id = UUID.randomUUID().toString();
        this.rentRentOfficeId = rentRentOfficeId;
        this.returnRentOfficeId = returnRentOfficeId;
        this.retrunTime = retrunTime;
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
