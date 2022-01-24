package com.example.bikesystem.api.dto.response;

import com.example.bikesystem.api.dto.TruckDTO;
import com.example.bikesystem.item.Truck;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TruckResponseDTO {

    private List<TruckDTO> truckList;

    public TruckResponseDTO(List<Truck> trucks) {
        this.truckList = new ArrayList<>();

        for (Truck truck : trucks) {
            this.truckList.add(TruckDTO.builder()
                    .id(truck.getSeq())
                    .location_id(truck.getLocationId())
                    .loaded_bikes_count(truck.getLoadBikeCnt())
                    .build());
        }

    }
}
