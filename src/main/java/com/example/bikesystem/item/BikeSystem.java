package com.example.bikesystem.item;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BikeSystem {

    private List<RentOffice> rentOffices;
    private List<User> users;
    private List<Truck> trucks;

    private String serverStatus;
    private Integer serverTime;
    private String truckTotalMoveDistance;

    private int truckCnt;
    private int initBikeCnt;

    public BikeSystem(ProblemType problemType) {

        this.rentOffices = new ArrayList<>();
        this.users = new ArrayList<>();
        this.trucks = new ArrayList<>();

        this.truckCnt = problemType.getTruckCnt();
        this.initBikeCnt = problemType.getInitBikeCnt();

        makeRentOffice(problemType.getxRange(), problemType.getyRange());
        makeTrucks();
    }


    /**
     * 대여소 생성
     * @param xRange
     * @param yRange
     */
    public void makeRentOffice(int xRange, int yRange) {
        int creatCnt = xRange * yRange;

        for (int i = 0; i < creatCnt; i++) {

            int xPosition = i / yRange;
            int yPosition = i % yRange;

            List<Bike> bikeList = IntStream.range(0, this.initBikeCnt).mapToObj(j -> new Bike()).collect(Collectors.toList());
            rentOffices.add(i, new RentOffice(bikeList, i ,xPosition, yPosition));
        }
    }

    /**
     * 트럭 생성(초기 매소드 실행)
     */
    public void makeTrucks(){
        this.trucks = IntStream.range(0, this.truckCnt).mapToObj(i -> new Truck(i)).collect(Collectors.toList());
    }


    /**
     * 사용자 생성
     * @param user
     */
    public void createUser(User user){
        users.add(user);
    }

    /**
     * 서버에 등록된 대여소 정보 조회
     * @return List<RentOffice>(대여소 리스트)
     */
    public List<RentOffice> getRentOffices() {
        return rentOffices;
    }


    /**
     * 서버에 등록된 트럭 정보 조회
     * @return List<Truck>(트럭 리스트)
     */
    public List<Truck> getTrucks() {
        return trucks;
    }


    public Truck getTruckInfo(int truckSeq){
        return trucks.get(truckSeq);
    }


    public RentOffice findRentOffice(String rentOfficeId){
        if(rentOffices.stream().anyMatch(rentOffice -> rentOffice.getId().equals(rentOfficeId))){
            return rentOffices.stream().filter(rentOffice -> rentOffice.getId().equals(rentOfficeId)).findFirst().get();
        }
        // 없는 경우 exception 처리 필요
        return new RentOffice();
    }


    public Rent rentBike(User user, String rentOfficeId, LocalTime localTime) {
        return new Rent(user, findRentOffice(rentOfficeId), localTime);
    }
}
