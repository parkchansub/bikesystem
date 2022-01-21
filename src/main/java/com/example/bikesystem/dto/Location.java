package com.example.bikesystem.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Location {

    private int id;
    private int located_bikes_count;

    @Builder
    public Location(int id,  int located_bikes_count) {
        this.id = id;
        this.located_bikes_count = located_bikes_count;
    }
}
