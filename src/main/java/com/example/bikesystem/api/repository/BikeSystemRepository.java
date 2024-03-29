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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class BikeSystemRepository {

    private BikeSystem system;
    private List<String> authKeyList;


    public BikeSystem getSystem() {
        return system;
    }

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
     *
     * @return LocationsResponseDTO
     */
    public LocationsResponseDTO getLocationInfo() {
        /*checkAuthCheck(authorization);*/
        return new LocationsResponseDTO(this.system.getRentOffices());

    }

    /**
     * 시스템 트럭 정보 조회
     *
     * @return TruckResponseDTO
     */
    public TruckResponseDTO getTrucksInfo() {
        /*checkAuthCheck(authorization);*/

        return new TruckResponseDTO(this.system.getTrucks());
    }


    public SimulateResponseDTO simulate(SimulateRequestDTO reqDto) {

        system.systemProgress();

        ActionItem resultItem;
        for (Command command : reqDto.getCommands()) {

            Truck truckInfo = system.findTruckInfo(command.getTruck_id());
            resultItem = new ActionItem(system.findRentOffice(command.getTruck_id()), truckInfo);
            for (Integer integer : command.getCommand()) {
                TruckMoveType truckMoveType = TruckMoveType.findTruckMoveType(integer);

                resultItem = (ActionItem) truckMoveType.getTruckInfo(ActionItem.builder()
                        .truck(truckInfo)
                        .rentOffice(system.findRentOffice(truckInfo.getLocationId()))
                        .build());

                system.updateBikeSystemRentOffice(resultItem);
            }
            //system.updateBikeSystemTruckInfo(truckInfo);
        }




       return SimulateResponseDTO.builder()
               .system(system)
               .build();
    }

    public void returnBike(Integer time) {
        system.returnBike(time);

    }

    public void sendServerTime() {
        system.sendTime();
    }


    /**
     * 자전거 대여 요청
     * @param requestItems
     */
    public void rent(List requestItems) {

        requestItems.stream().forEach(o -> {
            List item = (List) o;
            int rentOfficeId = (int) item.get(0);
            int returnOfficeId = (int) item.get(1);
            int returnTime = (int) item.get(2);

            RentOffice rentOffice = system.findRentOffice(rentOfficeId);
            if (rentOffice.isSatisfiedByLoadBike()) {
                system.rentBike(rentOffice.rentBike(returnTime, returnOfficeId));
            } else {
                system.updateFailRequestCnt(1);
            }

        });



    }


    /**
     * 요청 시간과 서버시간 비교
     * @param time
     * @return boolean
     */
    public boolean checkServerTime(Integer time) {

        return system.getServerTime().equals(time);
    }


    public BikeSystem getSystemInfo(){
        return this.system;
    }



    /**
     * 시간대 별 요청 bike 객체 생성 하여 대여 및 반납 map 함목 추가
     * @param rentRequests
     * @param requestTime
     */
    public void createRequestMap(List rentRequests, String requestTime) {

        List<Bike> bikes = new ArrayList<>();


        for (Object rentRequest : rentRequests) {
            /*(빌리는 대여소ID, 반납하는 대여소ID, 빌리는 시간(분))*/
            List requestItem = (List) rentRequest;

            Bike bike = Bike.builder()
                    .rentRentOfficeId((int) requestItem.get(0))
                    .returnRentOfficeId((int) requestItem.get(1))
                    .retrunTime((int) requestItem.get(2))
                    .build();

            system.addReturnBike(requestTime, bike);
            bikes.add(bike);
        }

        system.addRentBike(requestTime, bikes);
    }
}
