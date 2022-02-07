package com.example.bikesystem.item;

import java.util.List;
import java.util.UUID;

/**
 * 대여소 제약 사항
 *  대여소에는 자전거를 무한대 보관할 수 있다고 가정한다.
 *  사용자 제약 사항
 *  사용자는 트럭이 운영하는 시간 동안만 대여 요청을 할 수 있다.
 *  사용자는 대여 요청을 할 때 다음 정보를 모두 알려준다.[1]
 *  자전거를 대여할 자전거 대여소 ID
 *  자전거를 반납할 자전거 대여소 ID
 *  자전거를 탈 시간(분 단위, 소수점 없음)
 *  사용자는 정시 분(minute)에 대여 요청을 보낸다.
 *  즉, 12시 1분에 대여 요청을 할 수 있으나 12시 1분 30초에는 대여 요청을 하지 않는다.
 *  사용자가 요청을 보낸 시점에 자전거 대여소에 자전거가 부족하면 사용자가 보낸 요청은 자동으로 취소된다.
 *  자전거를 대여한 사용자는 반드시 대여 시 약속한 시각과 장소에 자전거를 반납하는 것으로 가정한다
 */
public class RentOffice {

    private String id;
    private int seq;
    private List<Bike> holdBikeList;

    private int xPosition;
    private int yPosition;

    private int requestCount;

    public int getSeq() {
        return seq;
    }
    public int getLocatedBikesCnt(){
        return holdBikeList.size();
    }

    public int getRequestCount() {
        return requestCount;
    }

    public RentOffice(List<Bike> bikes, int seq, int xPosition, int yPosition) {
        this.id = UUID.randomUUID().toString();
        holdBikeList = bikes;
        this.seq = seq;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Bike rentBike(Integer returnTime, Integer returnRentOfficeId){

        Bike bike = holdBikeList.get(0).rentBike(returnTime, returnRentOfficeId);
        holdBikeList.remove(bike);

        return bike;
    }



    public boolean isSatisfiedByLoadBike(){
        requestRent();
        return holdBikeList.size()>0 ? true : false;
    }


    public void requestRent(){
        this.requestCount = this.requestCount + 1;
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

    public void returnBike(User user) {
        getBike(user.getRentBike());
        user.returnBike();
    }
}
