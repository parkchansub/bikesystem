package com.example.bikesystem.api.dto.request;

import com.example.bikesystem.api.dto.Command;
import lombok.Getter;

import java.util.List;

@Getter
public class SimulateRequestDTO {

   private List<Command> commands;
}
