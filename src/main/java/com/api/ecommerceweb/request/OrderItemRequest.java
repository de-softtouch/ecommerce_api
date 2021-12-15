package com.api.ecommerceweb.request;

import com.api.ecommerceweb.model.Shop;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderItemRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long variantId;

    @NotNull
    private String message;

    @NotNull
    private Long shopId;

    @NotNull
    private Long addressId;

    @NotNull
    private Integer qty;

}
