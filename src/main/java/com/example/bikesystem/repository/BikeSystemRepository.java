package com.example.bikesystem.repository;

import com.example.bikesystem.api.dto.Command;
import com.example.bikesystem.api.dto.request.SimulateRequestDTO;
import com.example.bikesystem.api.dto.response.SimulateResponseDTO;
import com.example.bikesystem.api.dto.response.TruckResponseDTO;
import com.example.bikesystem.api.exception.ApiException;
import com.example.bikesystem.api.dto.response.LocationsResponseDTO;
import com.example.bikesystem.api.dto.request.RentBikeRequestDTO;
import com.example.bikesystem.api.dto.response.SystemStartResponseDTO;
import com.example.bikesystem.item.*;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class BikeSystemRepository {

    private BikeSystem system;
    private List<String> authKeyList;


    public SystemStartResponseDTO startBikeSystem(ProblemType problemType) {
        this.authKeyList = new ArrayList<>();
        this.system = new BikeSystem(problemType);

        String authKey = String.valueOf(UUID.randomUUID());
        authKeyList.add(authKey);

        return SystemStartResponseDTO.builder()
                .authKey(authKey)
                .problem(problemType.getProblemNum())
                .build();
    }

    private void checkAuthCheck(String authorization) {
        if(!authKeyList.contains(authorization)){
            throw new ApiException("등록되지 않은 키 값 입니다.");
        }
    }

    public Bike getRentBike(RentBikeRequestDTO rentBikeRequestDTO) {
        LocalTime localTime = null;
        User user = new User();

        system.createUser(user);
        system.rentBike(user, rentBikeRequestDTO.getRentOfficeId(), localTime);
        return new Bike();
    }


    public LocationsResponseDTO getLocationInfo(String authorization) {

        checkAuthCheck(authorization);

        return new LocationsResponseDTO(this.system.getRentOffices());

    }

    public TruckResponseDTO getTrucksInfo(String authorization) {
        checkAuthCheck(authorization);

        return new TruckResponseDTO(this.system.getTrucks());
    }


    public SimulateResponseDTO simulate(SimulateRequestDTO reqDto) {

        for (Command command : reqDto.getCommands()) {

            Truck truckInfo = system.getTruckInfo(command.getTruck_id());
        }
        return new SimulateResponseDTO();
    }
}
