package com.example.bikesystem.item;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Truck {

    private String id;
    private List<Bike> bikeList;

    private int locationId;
    private int seq;

    private int moveDistance;
    private int moveSeconds;
    private int failRequestCnt;


    /*가로 세로 최대 길이*/
    private int xRange;
    private int yRange;

    private final int COMMANDPROCESSSECONDS = 6;
    private final int COMMANDPROCESSDISTANCE = 100;




    public Truck(int seq, int xRange, int yRange ) {
        this.id = UUID.randomUUID().toString();
        this.bikeList = new ArrayList<>();

        this.seq = seq;
        this.locationId = 0;
        this.moveDistance = 0;
        this.xRange = xRange;
        this.yRange = yRange;
    }


    public int getLoadBikeCnt(){
        return bikeList.size();
    }

    /**
     * 명령에 성공 or 실패시 시간 변경
     */
    public void passMoveSeconds(){
        this.moveSeconds = this.moveSeconds + COMMANDPROCESSSECONDS;
    }

    /**
     * 요청 실패 건수 추가
     */
    public void addFailReqCnt() {
        this.failRequestCnt = this.failRequestCnt+1;
    }

    /**
     * 이동 관련 명령 실행
     * @param range
     */
    public void moveCammand(int range){
        if(this.locationId + range >-1){
            this.locationId = this.locationId + range;
            this.moveDistance = this.moveDistance+COMMANDPROCESSDISTANCE;
        }
        passMoveSeconds();
    }


    /**
     * 자전거 상차 가능 여부
     * @return boolean
     */
    public boolean isSatisfiedByLoadBike(){
        return bikeList.size()>0 ? true : false;
    }

    /**
     * 트럭 자전거 상차
     * @param bike
     */
    public void loadBike(Bike bike){
        bikeList.add(bike);
        passMoveSeconds();
    }


    /**
     * 트럭 자전거 하차
     * @return bike
     */
    public Bike dropBike() {
        Bike bike = bikeList.get(0);
        bikeList.remove(bike);
        passMoveSeconds();
        return bike;
    }


    public void initMoveSeconds(){
        this.moveSeconds = 0;
    }

}
