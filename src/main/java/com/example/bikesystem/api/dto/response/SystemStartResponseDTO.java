package com.example.bikesystem.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SystemStartResponseDTO {

    private String auth_key;
    private String problem;
    private String time;

    @Builder
    public SystemStartResponseDTO(String problem, String authKey) {
        this.auth_key = authKey;
        this.problem = problem;
        this.time = "0";
    }
}
