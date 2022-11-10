package com.example.bikesystem.api.dto.response;

import com.example.bikesystem.api.dto.LocationDTO;
import com.example.bikesystem.item.RentOffice;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LocationsResponseDTO {

    private List<LocationDTO> locations;
    private int totalRequestCnt;

    public LocationsResponseDTO(List<RentOffice> rentoffices) {
        this.locations = new ArrayList<>();
        rentoffices.stream()
                .forEach(rentOffice -> {
                    this.locations.add(LocationDTO.builder()
                            .id(rentOffice.getSeq())
                            .located_bikes_count(rentOffice.getLocatedBikesCnt())
                            .located_request_sucess_count(rentOffice.getRequestCount())
                            .build());
                });



        this.totalRequestCnt = locations.stream().mapToInt(location -> location.getLocated_request_sucess_count()).sum();
    }
}
