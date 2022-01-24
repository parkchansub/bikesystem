package com.example.bikesystem.api.dto.response;

import com.example.bikesystem.api.dto.LocationDTO;
import com.example.bikesystem.item.RentOffice;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LocationsResponseDTO {

    private List<LocationDTO> locations;

    public LocationsResponseDTO(List<RentOffice> rentoffices) {
        this.locations = new ArrayList<>();

        for (RentOffice rentoffice : rentoffices) {

            locations.add(LocationDTO.builder()
                    .id(rentoffice.getSeq())
                    .located_bikes_count(rentoffice.getLocatedBikesCnt())
                    .build());

        }
    }
}
