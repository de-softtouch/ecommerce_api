package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.enumm.EPaymentMethod;
import lombok.Data;

@Data
public class PaymentMethodDTO {

    private Long id;

    private EPaymentMethod EPaymentMethod;

    private int active;

}
