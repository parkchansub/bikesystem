package com.example.bikesystem.item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BikeSystem {

    /*운행 하는 트럭수*/
    private final int TRUCKCNT;

    /*초기 자전거 수*/
    private final int INITBIKECNT;

    /*대여소 장소 x축 최대 길이*/
    private final int XRANGE;

    /*대여소 장소 y축 최대 길이*/
    private final int YRANGE;

    /*대여소*/
    private List<RentOffice> rentOffices;

    /*시스템 사용자*/
    private List<User> users;

    /*트럭*/
    private List<Truck> trucks;

    /*서버 상태*/
    private String serverStatus;

    /*서버 시간*/
    private Integer serverTime;

    /*트럭 운행 거리*/
    private int truckTotalMoveDistance;

    /*요청 실패 건수*/
    private int failReuqestCnt;

    public int getTruckTotalMoveDistance() {

        truckTotalMoveDistance = trucks.stream().mapToInt(truck -> truck.getMoveDistance()).sum();

        return truckTotalMoveDistance;
    }

    public int getFailReuqestCnt() {
        return failReuqestCnt;
    }

    public Integer getServerTime() {
        return serverTime;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public BikeSystem(ProblemType problemType) {

        this.users = new ArrayList<>();
        this.rentOffices = new ArrayList<>();
        this.trucks = new ArrayList<>();

        this.TRUCKCNT = problemType.getTruckCnt();
        this.INITBIKECNT = problemType.getInitBikeCnt();
        this.XRANGE = problemType.getxRange();
        this.YRANGE = problemType.getyRange();
        this.serverTime =0;
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
        Truck truck = trucks.get(truckSeq);
        truck.initMoveSeconds();

        return truck;
    }

    /**
     * 서버에 등록된 특정 대여소 검색
     * @param rentOfficeSeq
     * @return RentOffice
     */
    public RentOffice findRentOffice(int rentOfficeSeq){
        return rentOffices.get(rentOfficeSeq);
    }


    /**
     * 시스템 트럭 및 대여소 정보 수정
     * @param actionItem
     * @return BikeSystem
     */
    public void updateBikeSystemRentOffice(ActionItem actionItem) {

        RentOffice modifyRentOffice = actionItem.getRentOffice();
        rentOffices.set(modifyRentOffice.getSeq(), modifyRentOffice);

    }

    public void updateBikeSystemTruckInfo(Truck truck){
        trucks.set(truck.getSeq(), truck);

        updateFailRequestCnt(truck.getFailRequestCnt());
    }

    /**
     * 대여 요청 실패시 시스템 실패 건수 수정
     * @param failCnt
     */
    public void updateFailRequestCnt(int failCnt){
        this.failReuqestCnt = this.failReuqestCnt + failCnt;
    }

    /**
     * 서버시간 경과
     * */
    public void sendTime(){
        this.serverTime = this.serverTime + 1;
    }


    /**
     * 자전거 반납
     */
    public void returnBike(){

        if(users.size()>0){
            List<User> userList = (List<User>) users.stream()
                    .filter(user -> user.getRentBike().getRetrunTime().equals(serverTime));

            for (User user : userList) {
                findRentOffice(user.getRentBike().getReturnRentOfficeId()).returnBike(user);
            }

        }
    }


    /**
     * 자전거 rent 요청
     */
    public void rentBike(Bike bike) {
        User user = new User();
        user.rentBike(bike);
        createUser(user);
    }


    public void systemProgress(){
        this.serverStatus = "in_progress";
    }




}
