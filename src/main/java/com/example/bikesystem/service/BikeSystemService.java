package com.example.bikesystem.service;

import com.example.bikesystem.api.dto.request.SimulateRequestDTO;
import com.example.bikesystem.api.dto.response.LocationsResponseDTO;
import com.example.bikesystem.api.dto.request.RentBikeRequestDTO;
import com.example.bikesystem.api.dto.response.SimulateResponseDTO;
import com.example.bikesystem.api.dto.response.SystemStartResponseDTO;
import com.example.bikesystem.api.dto.response.TruckResponseDTO;
import com.example.bikesystem.item.Bike;
import com.example.bikesystem.item.ProblemType;
import com.example.bikesystem.repository.BikeSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BikeSystemService {

    private final BikeSystemRepository bikeSystemRepository;

    public SystemStartResponseDTO startBikeSystem(String problem) {

        return bikeSystemRepository.startBikeSystem(ProblemType.findProblemType(problem));
    }

    public Bike rentBike(RentBikeRequestDTO rentBikeRequestDTO) {
        return bikeSystemRepository.getRentBike(rentBikeRequestDTO);
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
}
