package com.example.bikesystem.api.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LocationDTO {

    private int id;
    private int located_bikes_count;
    private int located_request_sucess_count;

    @Builder
    public LocationDTO(int id, int located_bikes_count, int located_request_sucess_count) {
        this.id = id;
        this.located_bikes_count = located_bikes_count;
        this.located_request_sucess_count = located_request_sucess_count;

    }
}
