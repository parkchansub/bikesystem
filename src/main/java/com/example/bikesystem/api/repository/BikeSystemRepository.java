package com.example.bikesystem.api.repository;

import com.example.bikesystem.api.dto.Command;
import com.example.bikesystem.api.dto.request.SimulateRequestDTO;
import com.example.bikesystem.api.dto.response.SimulateResponseDTO;
import com.example.bikesystem.api.dto.response.TruckResponseDTO;
import com.example.bikesystem.common.exception.ApiException;
import com.example.bikesystem.api.dto.response.LocationsResponseDTO;
import com.example.bikesystem.api.dto.response.SystemStartResponseDTO;
import com.example.bikesystem.item.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

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
            throw new ApiException(401,"인증을 위한 Header가 잘못됨");
        }
    }


    private void checkStartServerApi(){
        if(ObjectUtils.isEmpty(system)){
            throw new ApiException(500, "StartApi 가 실행 되지 않았습니다.");
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

                resultItem = truckMoveType.getTruckInfo(ActionItem.builder()
                        .truck(resultItem.getTruck())
                        .rentOffice(system.findRentOffice(resultItem.getTruck().getLocationId()))
                        .build());
            }
            system.updateBikeSystem(resultItem);
        }

        system.sendTime();


       return SimulateResponseDTO.builder()
               .system(system)
               .build();
    }

    public void rent(List requestItem, String time) {
        int rentOfficeId = (int) requestItem.get(0);
        int returnOfficeId = (int) requestItem.get(1);
        int returnTime = (int) requestItem.get(2);
        RentOffice rentOffice = system.findRentOffice(rentOfficeId);

        if (rentOffice.isSatisfiedByRent()) {
            system.rentBike(rentOffice.rentBike(returnTime, returnOfficeId));
        }
        else{
            system.updateFailRequestCnt(1);
        }

    }
}
