package com.example.bikesystem.item;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ActionItem {

    private int moveDistance;
    private int failRequestCnt;

    private RentOffice rentOffice;
    private Truck truck;

    private int xRange;
    private int yRange;


    public ActionItem() {
    }

    @Builder
    public ActionItem(RentOffice rentOffice, Truck truck) {
        this.rentOffice = rentOffice;
        this.truck = truck;

        this.xRange = truck.getXRange();
        this.yRange = truck.getYRange();

        this.moveDistance = 0;
        this.failRequestCnt = 0;

    }

    public ActionItem moveCommand(int range){
        truck.moveCammand(range);

        if (this.truck.getLocationId() > 0) {
            this.moveDistance = this.moveDistance+100;
        } else {
            this.failRequestCnt = this.failRequestCnt+1;
        }

        return this;
    }


    public ActionItem dropBike(){
        rentOffice.getBike(truck.dropBike());
        return this;
    }

    public ActionItem loadBike(){
        truck.loadBike(this.rentOffice.lostBike());
        return this;
    }
}
