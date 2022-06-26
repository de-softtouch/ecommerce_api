package com.api.ecommerceweb.dto;

import lombok.Data;

/**
 * variation of product
 */
@Data
public class VariationDTO {

    private Long id;

    private String name;

    private Double price;

    private Integer qty;


    private String description;

}
