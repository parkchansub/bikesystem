package com.example.bikesystem.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class Command {

    private int truck_id;
    private List<Integer> command;
}
