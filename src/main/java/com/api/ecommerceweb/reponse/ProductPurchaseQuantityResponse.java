package com.api.ecommerceweb.reponse;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPurchaseQuantityResponse implements Serializable {


    private Long modelId;

    private Long shopId;

    private Long productId;

    private String modelName;

    private Integer stock;

    private Integer maxPurchaseQuantity;

    private Double price;

    private Double priceBeforeDiscount;


}
