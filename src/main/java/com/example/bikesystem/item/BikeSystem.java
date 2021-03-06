package com.example.bikesystem.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /*반납 예정 자전거 hash*/
    private Map<String,List<Bike>> returnBikeMap;

    /*대여 예정 자전거 hash*/
    private Map<String,List<Bike>> rentBikeMap;


    /*서버 상태*/
    private String serverStatus;

    /*서버 시간*/
    private Integer serverTime;

    /*트럭 운행 거리*/
    private int truckTotalMoveDistance;

    /*요청 실패 건수*/
    private int failReuqestCnt;


    public Map<String, List<Bike>> getReturnBikeMap() {
        return returnBikeMap;
    }

    public Map<String, List<Bike>> getRentBikeMap() {
        return rentBikeMap;
    }

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

        this.rentBikeMap = new HashMap<>();
        this.returnBikeMap = new HashMap<>();

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
     * @param time
     */
    public void returnBike(Integer time){

        if(serverTime.equals(time)){
            if(users.size()>0){


                List<User> userList = (List<User>) users.stream()
                        .filter(user -> user.getRentBike().getRetrunTime().equals(serverTime));

                for (User user : userList) {
                    findRentOffice(user.getRentBike().getReturnRentOfficeId()).returnBike(user);

                    users.remove(user);
                }

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
        addReturnBikeMap(bike);




    }

    private void addReturnBikeMap(Bike bike) {

        String returnTime = String.valueOf(bike.getRetrunTime());

        if(returnBikeMap.containsKey(returnTime)){
            List<Bike> bikes = returnBikeMap.get(returnTime);
             bikes.add(bike);
             returnBikeMap.put(returnTime, bikes);
        }
    }

    /**
     * 반납 로직
     * 1. returnBikeMap 요청 시간에 반납해야 하는 자전거 정보를 return 하면서 해당 List<bike></bike> 삭제
     * 2. return 받은 자전거의 returnOfficeId 기준으로 대여소를 찾음
     * 3. 찾은 대여소에 해당 bike를 add 함
     * @param time
     */
    public void returnBike2(String time){

        if(returnBikeMap.containsKey(time)){
            returnBikeMap.get(time);
        }

    }


    public void systemProgress(){
        this.serverStatus = "in_progress";
    }


    /**
     * 대여 요청 Map 시간대별 자전거 add
     * @param requestTime
     * @param bikes
     */
    public void addRentBike(String requestTime, List<Bike> bikes){
        rentBikeMap.put(requestTime, bikes);
    }

    /**
     * 반납 예정 자전거 Map 자전거 add
     * @param requestTime
     * @param bike
     */
    public void addReturnBike(String requestTime, Bike bike) {

        String returnTime = String.valueOf(bike.getRetrunTime());
        List<Bike> returnBikes = new ArrayList<>();

        if (returnBikeMap.containsKey(requestTime)) {
            returnBikes = returnBikeMap.get(requestTime);
        }
        returnBikes.add(bike);
        returnBikeMap.put(returnTime, returnBikes);
    }

}
