package com.api.ecommerceweb.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PaymentDTO {

    private Long id;

    private int isSuccess;

    private PaymentMethodDTO paymentMethod;

    private Date createDate;

    private Date modifyDate;

}
