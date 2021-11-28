package com.api.ecommerceweb.enumm;


import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {

    CANCELLED(0), TO_PAY(1), TO_SHIP(2), TO_RECEIVE(3), COMPLETED(4);
    //    huy, cho xac nhan, cho lay hang, dang giao, da giao

    int code;

    OrderStatus(int code) {
        this.code = code;
    }


    public static Optional<OrderStatus> fromCode(int code) {
        return Arrays.stream(values())
                .filter(bl -> bl.code == code)
                .findFirst();
    }
}
