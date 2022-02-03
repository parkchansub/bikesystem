package com.example.bikesystem.item;


import java.util.Arrays;
import java.util.function.Function;

/**
 *  0: 6초간 아무것도 하지 않음
 *  1: 위로 한 칸 이동 -> 해당 트럭 객체의 locationId 변경(이동가능한지 여부 체크)
 *  2: 오른쪽으로 한 칸 이동
 *  3: 아래로 한 칸 이동
 *  4: 왼쪽으로 한 칸 이동
 *  5: 자전거 상차
 *  6: 자전거 하차
 * */
public enum TruckMoveType {

    STOP(0, actionItem -> actionItem),
    MOVEUP(1, actionItem -> actionItem.moveCommand(1)),
    MOVERIGHT(2, actionItem -> actionItem.moveCommand(actionItem.getYRange())),
    MOVEDOWN(3, actionItem -> actionItem.moveCommand(-1)),
    MOVELEFT(4, actionItem -> actionItem.moveCommand(-actionItem.getYRange())),
    LOADBIKE(5, actionItem -> actionItem.loadBike()),
    DROPBIKE(6, actionItem -> actionItem.dropBike());

    private int typeCode;
    private Function<ActionItem, ActionItem> expression;

    TruckMoveType(int typeCode, Function<ActionItem, ActionItem> expression) {
        this.typeCode = typeCode;
        this.expression = expression;
    }

    public ActionItem getTruckInfo(ActionItem actionItem){
        return expression.apply(actionItem);
    }

    public int getTypeCode(){
        return typeCode;
    }

    public static TruckMoveType findTruckMoveType(int typeCode){
        return Arrays.stream(TruckMoveType.values())
                .filter(truckMoveType -> truckMoveType.getTypeCode() == typeCode)
                .findFirst()
                .orElse(STOP);
    }





}
