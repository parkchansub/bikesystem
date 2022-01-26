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
    private int failRequestCnt;


    /*가로 세로 최대 길이*/
    private int xRange;
    private int yRange;


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


    public void moveCammand(int range){
        if(this.locationId + range > 0){
            this.locationId = this.locationId + range;
            this.moveDistance = this.moveDistance+100;
        }else{
            addFailReqCnt();
        }

    }

    public void addFailReqCnt() {
        this.failRequestCnt = this.failRequestCnt+1;
    }

    public void loadBike(Bike bike){
        bikeList.add(bike);
    }

    public boolean isSatisfiedByDropBike(){
        return bikeList.size()>0 ? true : false;
    }



    public Bike dropBike() {
        Bike bike = bikeList.get(0);
        bikeList.remove(bike);

        return bike;
    }

}
