package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModelResponse {

    private Long id;

    private String name;

    private Double price;

    private Double priceBeforeDiscount;

    private Integer stock;

    private Integer status;

    private List<Long> variantOptionIndexes = new LinkedList<>();


}
