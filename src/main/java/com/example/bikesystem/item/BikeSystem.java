package com.example.bikesystem.item;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BikeSystem {

    private List<RentOffice> rentOffices;
    private List<User> users;

    private int truckCnt;
    private int initBikeCnt;

    public BikeSystem(ProblemType problemType) {

        this.rentOffices = new ArrayList<>();
        this.users = new ArrayList<>();

        this.truckCnt = problemType.getTruckCnt();
        this.initBikeCnt = problemType.getInitBikeCnt();

        makeRentOffice(problemType.getxRange(), problemType.getyRange());
    }

    public void createUser(User user){
       users.add(user);
    }


    /*public BikeSystem(int truckCnt, int initBikeCnt, int xRange, int yRange) {
        this.truckCnt = truckCnt;
        this.initBikeCnt = initBikeCnt;
        this.rentOffices = new ArrayList<>();
        this.users = new ArrayList<>();

        makeRentOffice(xRange, yRange);
    }*/

    public void makeRentOffice(int xRange, int yRange) {
        int creatCnt = xRange * yRange;

        for (int i = 0; i < creatCnt; i++) {

            int xPosition = i / yRange;
            int yPosition = i % yRange;

            List<Bike> bikeList = IntStream.range(0, this.initBikeCnt).mapToObj(j -> new Bike()).collect(Collectors.toList());
            rentOffices.add(i, new RentOffice(bikeList, i ,xPosition, yPosition));
        }
    }


    public Rent rentBike(User user, String rentOfficeId, LocalTime localTime) {
        return new Rent(user, findRentOffice(rentOfficeId), localTime);
    }



    public RentOffice findRentOffice(String rentOfficeId){
        if(rentOffices.stream().anyMatch(rentOffice -> rentOffice.getId().equals(rentOfficeId))){
            return rentOffices.stream().filter(rentOffice -> rentOffice.getId().equals(rentOfficeId)).findFirst().get();
        }
        // 없는 경우 exception 처리 필요
        return new RentOffice();
    }

    public List<RentOffice> getRentOffices() {
        return rentOffices;
    }
}
