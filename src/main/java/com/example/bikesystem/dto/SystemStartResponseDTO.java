package com.example.bikesystem.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SystemStartResponseDTO {

    private String auth_key;
    private String problem;
    private String time;

    @Builder

    public SystemStartResponseDTO(String problem) {
        this.auth_key = String.valueOf(UUID.randomUUID());
        this.problem = problem;
        this.time = "0";
    }
}
