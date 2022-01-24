package com.example.bikesystem.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TruckDTO {

    private int id;
    private int location_id;
    private int loaded_bikes_count;

    @Builder
    public TruckDTO(int id, int location_id, int loaded_bikes_count) {
        this.id = id;
        this.location_id = location_id;
        this.loaded_bikes_count = loaded_bikes_count;
    }
}
