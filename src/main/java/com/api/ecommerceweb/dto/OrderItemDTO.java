package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.model.Product;
import lombok.Data;

@Data
public class OrderItemDTO {

    private Long id;

    private Integer qty;

    private VariationDTO variation;

    private Product product;

    private String message;

}
