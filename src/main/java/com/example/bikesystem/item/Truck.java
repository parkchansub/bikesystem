package com.example.bikesystem.item;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *  트럭 제약 사항
 *  트럭은 바둑판 모양의 길을 따라서 상하좌우로만 이동한다. 즉, 대각선 방향으로는 이동할 수 없다.
 *  단, 트럭은 서비스 지역을 벗어날 수 없다.
 *  트럭은 매일 아침 오전 10시에 ID가 0인 자전거 대여소에 모여 있으며, 오후 10시까지만 운행한다.
 *  트럭은 아무 곳에서나 운행을 종료할 수 있으며, 도중에 멈춰 서는 것도 가능하다.
 *  트럭이 100m를 이동하는 데에는 6초가 걸린다. 즉 트럭은 격자 한 칸을 6초 만에 이동할 수 있다.
 *  트럭이 자전거 하나를 싣거나 내리는 데에는 6초가 걸린다.
 *  각 트럭은 자전거를 싣거나, 내리거나, 또는 길을 이동할 수 있다. 단, 한 번에 한 가지 일만 해야 한다.
 *  예를 들어, 12시 00분 00초에 자전거를 내리기 시작하면 12시 00분 6초부터 길을 달리거나 다른 자전거를 싣거나 내릴 수 있다.
 *  12시 00분에 자전거 두 대를 동시에 내리거나, 길을 달림과 동시에 자전거를 내릴 수는 없다.
 *  각 트럭은 자전거를 최대 20대 수용할 수 있다.
 *  다음은 트럭이 이동 가능한 방향에 대한 예시이다.
 */
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
