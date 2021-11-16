package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.enumm.OrderStatus;
import com.api.ecommerceweb.model.*;
import lombok.Data;

import java.util.*;

@Data
public class OrderDTO {

    private Long id;

    private PaymentDTO payment;

    private OrderStatus status;

    private AddressDTO address;

    private User user;

    private Date orderDate;

    private List<OrderSellerMessageDTO> sellerMessages = new ArrayList<>();

    private Set<OrderItemDTO> orderItems = new HashSet<>();

}
