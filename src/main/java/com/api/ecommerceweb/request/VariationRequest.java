package com.api.ecommerceweb.request;

import lombok.Data;

@Data
public class VariationRequest {

    private Long id;

    private Double price;

    private Integer qty;

    private ColorRequest color;

    private SizeRequest size;
}
