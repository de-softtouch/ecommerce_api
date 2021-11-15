package com.api.ecommerceweb.enumm;

public enum EPaymentMethod {

    COD(1), CREDIT(2), DEBIT(3);

      int code;

    EPaymentMethod(int code) {
        this.code = code;
    }
}
