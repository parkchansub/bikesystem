package com.example.bikesystem.item;

import java.util.Arrays;

public enum ProblemType {
    ONE("1",5,3,5,5),
    TWO("2",10,3, 60,60);

    String problemNum;
    int truckCnt;
    int initBikeCnt;
    int xRange;
    int yRange;

    ProblemType(String problemNum, int truckCnt, int initBikeCnt, int xRange, int yRange) {
        this.problemNum = problemNum;
        this.truckCnt = truckCnt;
        this.initBikeCnt = initBikeCnt;
        this.xRange = xRange;
        this.yRange = yRange;
    }


    public static ProblemType findProblemType(String problemNum){
        return Arrays.stream(ProblemType.values())
                .filter(problemType -> problemType.problemNum.equals(problemNum))
                .findFirst()
                .orElse(ONE);
    }

    public String getProblemNum() {
        return problemNum;
    }

    public int getTruckCnt() {
        return truckCnt;
    }

    public int getInitBikeCnt() {
        return initBikeCnt;
    }

    public int getxRange() {
        return xRange;
    }

    public int getyRange() {
        return yRange;
    }
}
