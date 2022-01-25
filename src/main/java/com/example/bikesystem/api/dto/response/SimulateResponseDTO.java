package com.example.bikesystem.api.dto.response;

import com.example.bikesystem.item.BikeSystem;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SimulateResponseDTO {

    private String status;
    private Integer time;
    private Integer failed_requests_count;
    private String distance;

    public SimulateResponseDTO() {
    }

    @Builder
    public SimulateResponseDTO(BikeSystem system) {
        this.status = "ready";
        this.time = +1;
        this.failed_requests_count = system.getFailReuqestCnt();
        this.distance = String.valueOf(system.getTruckTotalMoveDistance());
    }
}
