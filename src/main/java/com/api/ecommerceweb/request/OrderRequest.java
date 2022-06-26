package com.api.ecommerceweb.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {


    @NotNull
    private Long paymentId;

    private Long addressId;

    @NotEmpty
    private Set<OrderItemRequest> items = new HashSet<>();


}
