package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.model.Color;
import com.api.ecommerceweb.model.Size;
import lombok.Data;

/**
 * variation of product
 */
@Data
public class VariationDTO {

    private Long id;

    private Double price;

    private Integer qty;

    private Size size;

    private Color color;

}
