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

    /**
     * 서버 실행
     * @param problemType
     * @return SystemStartResponseDTO
     */
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


    /**
     * 시스템 대여소 정보 조회
     * @param authorization
     * @return LocationsResponseDTO
     */
    public LocationsResponseDTO getLocationInfo(String authorization) {
        checkAuthCheck(authorization);
        return new LocationsResponseDTO(this.system.getRentOffices());

    }

    /**
     * 시스템 트럭 정보 조회
     * @param authorization
     * @return TruckResponseDTO
     */
    public TruckResponseDTO getTrucksInfo(String authorization) {
        checkAuthCheck(authorization);

        return new TruckResponseDTO(this.system.getTrucks());
    }


    public SimulateResponseDTO simulate(SimulateRequestDTO reqDto) {

        system.systemProgress();
        system.returnBike();

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

    /**
     * 자전거 대여 요청
     * @param requestItem
     */
    public void rent(List requestItem) {
        int rentOfficeId = (int) requestItem.get(0);
        int returnOfficeId = (int) requestItem.get(1);
        int returnTime = (int) requestItem.get(2);

        RentOffice rentOffice = system.findRentOffice(rentOfficeId);

        if (rentOffice.isSatisfiedByLoadBike()) {
            system.rentBike(rentOffice.rentBike(returnTime, returnOfficeId));
        }
        else{
            system.updateFailRequestCnt(1);
        }

    }

    /**
     * 요청 시간과 서버시간 비교
     * @param time
     * @return boolean
     */
    public boolean checkServerTime(String time) {
        return system.getServerTime().equals(time);
    }
}
