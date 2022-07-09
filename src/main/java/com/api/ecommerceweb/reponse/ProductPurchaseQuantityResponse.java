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

    private Integer stock = 0;

    private Integer maxPurchaseQuantity = 0;

    private Double salesPrice = 0.0;

    private Double standardPrice = 0.0;


}
