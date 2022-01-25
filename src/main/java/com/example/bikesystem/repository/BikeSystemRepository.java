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



    public LocationsResponseDTO getLocationInfo(String authorization) {

        checkAuthCheck(authorization);

        return new LocationsResponseDTO(this.system.getRentOffices());

    }

    public TruckResponseDTO getTrucksInfo(String authorization) {
        checkAuthCheck(authorization);

        return new TruckResponseDTO(this.system.getTrucks());
    }


    public SimulateResponseDTO simulate(SimulateRequestDTO reqDto) {

        ActionItem resultItem;
        for (Command command : reqDto.getCommands()) {

            resultItem = new ActionItem(system.findRentOffice(command.getTruck_id()), system.findTruckInfo(command.getTruck_id()));
            for (Integer integer : command.getCommand()) {

                TruckMoveType truckMoveType = TruckMoveType.findTruckMoveType(integer);

                System.out.println("truck info : "+ resultItem.getTruck().toString());
                System.out.println("truck location : "+ resultItem.getRentOffice().toString());
                System.out.println("---------------------------------------");


                resultItem = truckMoveType.getTruckInfo(ActionItem.builder()
                        .truck(resultItem.getTruck())
                        .rentOffice(system.findRentOffice(resultItem.getTruck().getLocationId()))
                        .build());

            }
            system.updateBikeSystem(resultItem);
        }



       return SimulateResponseDTO.builder()
               .system(system)
               .build();
    }
}
