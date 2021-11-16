package com.api.ecommerceweb.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderSellerMessageDTO {

    private Long id;

    private String message;

    private int posIndex;

    private Date orderDate;

    private Date updateDate;
}
