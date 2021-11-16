package com.api.ecommerceweb.dto;

import lombok.Data;

import java.util.Date;

@Data
public class FeedbackDTO {

    private Long id;

    private String comment;

    private Integer rating;

    private Integer qualityRating;

    private Integer shipmentRating;

    private Integer sellerRating;

    private Date createDate;

    private Date modifyDate;

    private UserDTO user;

    private ProductDTO product;

    private OrderItemDTO orderItem;
}
