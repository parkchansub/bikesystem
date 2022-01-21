package com.example.bikesystem.service;

import com.example.bikesystem.dto.LocationsResponseDTO;
import com.example.bikesystem.dto.RentBikeRequestDTO;
import com.example.bikesystem.dto.SystemStartResponseDTO;
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
}
