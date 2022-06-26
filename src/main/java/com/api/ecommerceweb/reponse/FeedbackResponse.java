package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private  Long id;

    private String comment;

    private int rating;

    private Date createDate;

    private Integer qualityRating;

    private Integer shipmentRating;

    private Integer sellerRating;


    private BasicUserInfoResponse user;
}
