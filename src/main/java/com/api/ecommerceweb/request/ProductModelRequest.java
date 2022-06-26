package com.api.ecommerceweb.request;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ProductModelRequest {

    private Long id;

    private String name;

    private List<Long> variantOptionIndexes = new LinkedList<>();

    private Integer stock;

    private Double price;

    private Double priceBeforeDiscount;

    private Integer status;
}
