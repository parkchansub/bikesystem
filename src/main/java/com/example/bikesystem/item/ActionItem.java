package com.example.bikesystem.item;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ActionItem {


    private RentOffice rentOffice;
    private Truck truck;

    private int xRange;
    private int yRange;

    @Builder
    public ActionItem(RentOffice rentOffice, Truck truck) {
        this.rentOffice = rentOffice;
        this.truck = truck;

        this.xRange = truck.getXRange();
        this.yRange = truck.getYRange();

    }

    /**
     * 트럭 이동 요청 명령 실행
     * @param range
     * @return
     */
    public ActionItem moveCommand(int range){
        truck.moveCammand(range);
        return this;
    }

    /**
     * 트럭 자전거 하차
     * @return ActionItem
     */
    public ActionItem dropBike(){
        if(truck.isSatisfiedByLoadBike()){
            rentOffice.getBike(truck.dropBike());
        }else{
            truck.addFailReqCnt();
        }
        return this;
    }


    /**
     * 트럭 자전거 상차
     * @return ActionItem
     */
    public ActionItem loadBike(){
        if(this.rentOffice.isSatisfiedByLoadBike()){
            truck.loadBike(this.rentOffice.lostBike());
        }else{
            truck.addFailReqCnt();
        }
        return this;
    }
}
