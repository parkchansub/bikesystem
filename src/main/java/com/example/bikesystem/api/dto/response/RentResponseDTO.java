package com.example.bikesystem.api.dto.response;

import com.example.bikesystem.item.BikeSystem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RentResponseDTO {
    private String flag;
    private int failCnt;

    @Builder
    public RentResponseDTO(BikeSystem system) {
        this.flag = "sucess";
        this.failCnt = system.getFailReuqestCnt();


    }
}
