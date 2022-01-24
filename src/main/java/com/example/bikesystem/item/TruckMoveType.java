package com.example.bikesystem.item;


import com.example.bikesystem.repository.BikeSystemRepository;

import java.util.function.Function;

/**
 *  0: 6초간 아무것도 하지 않음
 *  1: 위로 한 칸 이동
 *  2: 오른쪽으로 한 칸 이동
 *  3: 아래로 한 칸 이동
 *  4: 왼쪽으로 한 칸 이동
 *  5: 자전거 상차
 *  6: 자전거 하차
 * */
public enum TruckMoveType {
    STOP(0, truck -> truck.moveActionTruck(0)),
    MOVEUP(1,truck -> truck.moveActionTruck(1)),
    MOVERIGHT(2, truck -> truck.moveActionTruck(2)),
    MOVEDOWN(3, truck -> truck.moveActionTruck(0)),
    MOVELEFT(4, truck -> truck.moveActionTruck(0)),
    LOADBIKE(5, truck -> truck.moveActionTruck(0)),
    DROPBIKE(6, truck -> truck.moveActionTruck(0));

    private int typeCode;
    private Function<Truck, Truck> expression;

    public Truck playCommand(Truck truck){
        return expression.apply(truck);
    }


    TruckMoveType(int typeCode, Function<Truck, Truck> expression) {
        this.typeCode = typeCode;
        this.expression = expression;
    }
}
