package com.api.ecommerceweb.reponse;

import com.api.ecommerceweb.dto.AddressDTO;
import com.api.ecommerceweb.enumm.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponse implements Serializable {

    private Long id;

    private List<OrderItemResponse> items = new ArrayList<>();

    private OrderStatus status;

    private AddressDTO address;

    private Date createDate;
}
