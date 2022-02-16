package com.example.bikesystem.api.dto.response;

import com.example.bikesystem.item.Bike;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PastRentResponseDTO {


    private Map<String, List<Bike>> returnBikeMap;
    private Map<String,List<Bike>> rentBikeMap;

    @Builder
    public PastRentResponseDTO(Map<String, List<Bike>> returnBikeMap, Map<String, List<Bike>> rentBikeMap) {
        this.returnBikeMap = returnBikeMap;
        this.rentBikeMap = rentBikeMap;
    }


}
