package com.api.ecommerceweb.model;

import com.api.ecommerceweb.enumm.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemStatusReport implements Serializable {

    //    @Enumerated(value = EnumType.)
    private Integer status;

    private Integer count;


}
