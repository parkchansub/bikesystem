package com.example.bikesystem.repository;

import com.example.bikesystem.dto.RentBikeRequestDTO;
import com.example.bikesystem.dto.SystemStartResponseDTO;
import com.example.bikesystem.item.Bike;
import com.example.bikesystem.item.BikeSystem;
import com.example.bikesystem.item.ProblemType;
import com.example.bikesystem.item.User;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public class BikeSystemRepository {

    private BikeSystem system;

    public SystemStartResponseDTO startBikeSystem(ProblemType problemType) {

        this.system = new BikeSystem(problemType);

        return SystemStartResponseDTO.builder()
                .problem(problemType.getProblemNum())
                .build();
    }

    public Bike getRentBike(RentBikeRequestDTO rentBikeRequestDTO) {
        LocalTime localTime = null;
        User user = new User();
        system.rentBike(user, rentBikeRequestDTO.getRentOfficeId(), localTime);
        return new Bike();
    }


}
