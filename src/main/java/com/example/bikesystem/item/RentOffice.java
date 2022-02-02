package com.example.bikesystem.item;

import java.util.List;
import java.util.UUID;

public class RentOffice {

    private String id;
    private int seq;
    private List<Bike> holdBikeList;

    private int xPosition;
    private int yPosition;


    public RentOffice() {
    }


    public RentOffice(List<Bike> bikes, int seq,int xPosition, int yPosition) {
        this.id = UUID.randomUUID().toString();
        holdBikeList = bikes;
        this.seq = seq;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Bike rentBike(Integer returnTime, Integer returnRentOfficeId){

         return holdBikeList.get(0).rentBike(returnTime,returnRentOfficeId);
    }


    public boolean isSatisfiedByRent(){
        return holdBikeList.size()> 0 ?  true : false;
    }

    public String getId() {
        return id;
    }

    public int getSeq() {
        return seq;
    }

    public int getLocatedBikesCnt(){
        return holdBikeList.size();
    }



    public boolean isSatisfiedByLostBike(){
        return holdBikeList.size()>0 ? true : false;
    }

    public Bike lostBike(){
        Bike bike = holdBikeList.get(0);
        holdBikeList.remove(bike);
        return bike;

    }




    public RentOffice getBike(Bike bike){
        holdBikeList.add(bike);
        return this;
    }

    public void returnBike(Integer serverTime) {
       /* holdBikeList.stream()
                .filter(bike -> bike.getRetrunTime().equals(serverTime))
                .*/

    }
}
