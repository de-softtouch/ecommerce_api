package com.api.ecommerceweb.request;

import com.api.ecommerceweb.model.Shop;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemRequest implements Serializable {

    @NotNull
    private Long productId;

    private Long modelId;

    private String message;

    @NotNull
    private Integer qty = 1;

}
