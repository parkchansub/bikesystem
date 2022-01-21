package com.example.bikesystem.dto;

import com.example.bikesystem.item.RentOffice;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LocationsResponseDTO {

    private List<Location> locations;

    public LocationsResponseDTO(List<RentOffice> rentoffices) {
        this.locations = new ArrayList<>();

        for (RentOffice rentoffice : rentoffices) {

            locations.add(Location.builder()
                    .id(rentoffice.getSeq())
                    .located_bikes_count(rentoffice.getLocatedBikesCnt())
                    .build());

        }
    }
}
