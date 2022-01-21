package com.example.bikesystem.item;

import java.time.LocalDateTime;
import java.util.UUID;

public class Bike {

    private String id;
    private LocalDateTime retrunTime;

    public Bike() {
        this.id = UUID.randomUUID().toString();
    }
}
