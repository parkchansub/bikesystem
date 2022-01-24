package com.example.bikesystem.api.dto.response;

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
    public SimulateResponseDTO(String status, Integer time, Integer failed_requests_count, String distance) {
        this.status = status;
        this.time = time;
        this.failed_requests_count = failed_requests_count;
        this.distance = distance;
    }
}
