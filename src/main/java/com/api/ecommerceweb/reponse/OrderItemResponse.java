package com.api.ecommerceweb.reponse;

import com.api.ecommerceweb.enumm.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponse implements Serializable {

    private Long id;

    private OrderStatus status;

    private Integer qty;

    private String message;

    private ProductModelResponse model;

    private BaseProductResponse product;
}
