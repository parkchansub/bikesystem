package com.example.bikesystem.item;

import java.time.LocalTime;

public class Rent {

    private User user;
    private RentOffice rentOffice;
    private LocalTime rentTime;


    public Rent(User user, RentOffice rentOffice, LocalTime localDateTime) {
        this.user = user;
        this.rentOffice = rentOffice;
        this.rentTime = localDateTime;
    }
}
