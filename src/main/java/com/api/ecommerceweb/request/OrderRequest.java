package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long shopId;

    @NotNull
    private Long paymentId;

    @NotEmpty
    private Set<OrderItemRequest> items = new HashSet<>();

}
