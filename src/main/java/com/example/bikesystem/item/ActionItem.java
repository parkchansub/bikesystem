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

    public ActionItem moveCommand(int range){
        truck.moveCammand(range);
        return this;
    }


    public ActionItem dropBike(){
        if(truck.isSatisfiedByDropBike()){
            rentOffice.getBike(truck.dropBike());
        }else{
            truck.addFailReqCnt();
        }
        return this;
    }




    public ActionItem loadBike(){

        if(this.rentOffice.isSatisfiedByLostBike()){
            truck.loadBike(this.rentOffice.lostBike());
        }else{
            truck.addFailReqCnt();
        }
        return this;
    }
}
