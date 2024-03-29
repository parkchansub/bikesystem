package com.example.bikesystem.api.service;

import com.example.bikesystem.api.dto.request.SimulateRequestDTO;
import com.example.bikesystem.api.dto.response.*;
import com.example.bikesystem.common.exception.ApiException;
import com.example.bikesystem.item.BikeSystem;
import com.example.bikesystem.item.ProblemType;
import com.example.bikesystem.api.repository.BikeSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BikeSystemService {

    private final BikeSystemRepository bikeSystemRepository;

    public SystemStartResponseDTO startBikeSystem(String problem) {

        Optional<ProblemType> problemType = ProblemType.findProblemType(problem);
        if(problemType.isEmpty()){
            throw new ApiException(400,"problem 값 잘못됨");
        }
        return bikeSystemRepository.startBikeSystem(problemType.get());
    }

    public LocationsResponseDTO getLocationInfo() {

        return bikeSystemRepository.getLocationInfo();
    }

    public TruckResponseDTO getTrucksInfo() {
        return bikeSystemRepository.getTrucksInfo();
    }

    public SimulateResponseDTO simulate(SimulateRequestDTO reqDto) {
        return bikeSystemRepository.simulate(reqDto);
    }


    public RentResponseDTO rent(Map<String, List> reqDto) {

        reqDto.forEach((time, rentReq) -> {
            bikeSystemRepository.returnBike(Integer.valueOf(time));
            bikeSystemRepository.rent(rentReq);
        });

        return RentResponseDTO.builder()
                .system(bikeSystemRepository.getSystemInfo())
                .build();


    }



    public PastRentResponseDTO createRequestMap(Map<String, List> reqDto) {

        for (String time : reqDto.keySet()) {
            /*요청 시간에 들어온 rent 요청*/
            List rentRequsets = reqDto.get(time);
            bikeSystemRepository.createRequestMap(rentRequsets, time);
        }

        BikeSystem system = bikeSystemRepository.getSystem();

        return PastRentResponseDTO.builder()
                .rentBikeMap(system.getRentBikeMap())
                .returnBikeMap(system.getReturnBikeMap())
                .build();

    }
}
