package com.api.ecommerceweb.reponse;

import lombok.*;

import java.io.Serializable;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductModelResponse implements Serializable {

    private Long id;

    private String name;

    private Double price;

    private Double priceBeforeDiscount;

    private Integer stock;

    private Integer status;

    private List<Long> variantOptionIndexes = new LinkedList<>();


}
