package com.api.ecommerceweb.enumm;

public enum OrderStatus {

    CANCELLED(0), TO_PAY(1), TO_SHIP(2), TO_RECEIVE(3), COMPLETED(4);
    //    huy, cho xac nhan, cho lay hang, dang giao, da giao

    int code;

    OrderStatus(int code) {
        this.code = code;
    }
}
