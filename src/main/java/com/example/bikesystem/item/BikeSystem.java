package com.example.bikesystem.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BikeSystem {

    private final int TRUCKCNT;
    private final int INITBIKECNT;

    private final int XRANGE;
    private final int YRANGE;

    private List<RentOffice> rentOffices;
    private List<User> users;
    private List<Truck> trucks;

    private String serverStatus;
    private Integer serverTime;
    private int truckTotalMoveDistance;
    private int failReuqestCnt;


    public BikeSystem(ProblemType problemType) {

        this.rentOffices = new ArrayList<>();
        this.users = new ArrayList<>();
        this.trucks = new ArrayList<>();

        this.TRUCKCNT = problemType.getTruckCnt();
        this.INITBIKECNT = problemType.getInitBikeCnt();
        this.XRANGE = problemType.getxRange();
        this.YRANGE = problemType.getyRange();
        this.truckTotalMoveDistance = 0;
        this.failReuqestCnt = 0;

        makeRentOffice();
        makeTrucks();
    }


    /**
     * 대여소 생성
     */
    public void makeRentOffice() {
        int creatCnt = XRANGE * YRANGE;

        for (int i = 0; i < creatCnt; i++) {

            int xPosition = i / YRANGE;
            int yPosition = i % YRANGE;

            List<Bike> bikeList = IntStream.range(0, this.INITBIKECNT).mapToObj(j -> new Bike()).collect(Collectors.toList());
            rentOffices.add(i, new RentOffice(bikeList, i ,xPosition, yPosition));
        }
    }

    /**
     * 트럭 생성(초기 매소드 실행)
     */
    public void makeTrucks(){
        this.trucks = IntStream.range(0, this.TRUCKCNT).mapToObj(i -> new Truck(i, XRANGE, YRANGE)).collect(Collectors.toList());
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
     * 서버에 등록된 트럭 리스트 정보 조회
     * @return List<Truck>(트럭 리스트)
     */
    public List<Truck> getTrucks() {
        return trucks;
    }

    /**
     * 서버에 등록된 특정 트럭 검색
     * @param truckSeq
     * @return
     */
    public Truck findTruckInfo(int truckSeq){
        return trucks.get(truckSeq);
    }

    /**
     * 서버에 등록된 특정 대여소 검색
     * @param rentOfficeSeq
     * @return
     */
    public RentOffice findRentOffice(int rentOfficeSeq){
        return rentOffices.get(rentOfficeSeq);
    }


    public BikeSystem updateBikeSystem(ActionItem actionItem) {
        trucks.set(actionItem.getTruck().getLocationId(), actionItem.getTruck());
        rentOffices.set(actionItem.getRentOffice().getSeq(), actionItem.getRentOffice());

        this.truckTotalMoveDistance = +actionItem.getMoveDistance();
        this.failReuqestCnt = +actionItem.getFailRequestCnt();
        return this;
    }

    public int getTruckTotalMoveDistance() {
        return truckTotalMoveDistance;
    }

    public int getFailReuqestCnt() {
        return failReuqestCnt;
    }

    public RentOffice findRentOffice(String rentOfficeId){
        if(rentOffices.stream().anyMatch(rentOffice -> rentOffice.getId().equals(rentOfficeId))){
            return rentOffices.stream().filter(rentOffice -> rentOffice.getId().equals(rentOfficeId)).findFirst().get();
        }
        // 없는 경우 exception 처리 필요
        return new RentOffice();
    }

}
