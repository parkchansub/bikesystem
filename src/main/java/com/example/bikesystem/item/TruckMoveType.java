package com.example.bikesystem.item;


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
    STOP(0),
    MOVEUP(1),
    MOVERIGHT(2),
    MOVEDOWN(3),
    MOVELEFT(4),
    LOADBIKE(5),
    DROPBIKE(6);

    private int typeCode;

    private


    TruckMoveType(int typeCode) {
        this.typeCode = typeCode;
    }
}
