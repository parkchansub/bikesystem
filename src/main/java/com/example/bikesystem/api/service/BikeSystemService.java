package com.example.bikesystem.api.service;

import com.example.bikesystem.api.dto.request.SimulateRequestDTO;
import com.example.bikesystem.api.dto.response.*;
import com.example.bikesystem.common.exception.ApiException;
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

    public LocationsResponseDTO getLocationInfo(String authorization) {

        return bikeSystemRepository.getLocationInfo(authorization);
    }

    public TruckResponseDTO getTrucksInfo(String authorization) {
        return bikeSystemRepository.getTrucksInfo(authorization);
    }

    public SimulateResponseDTO simulate(SimulateRequestDTO reqDto) {
        return bikeSystemRepository.simulate(reqDto);
    }


    public RentResponseDTO rent(Map<String, List> reqDto) {
        for (String time : reqDto.keySet()) {
            if(bikeSystemRepository.checkServerTime(time)){
                List list = reqDto.get(time);
                for (Object o : list) {
                    /*(빌리는 대여소ID, 반납하는 대여소ID, 빌리는 시간(분))*/
                    List requestItem = (List) o;
                    bikeSystemRepository.rent(requestItem);
                }
            }
        }
        return new RentResponseDTO();
    }
}
