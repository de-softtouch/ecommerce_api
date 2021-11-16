package com.api.ecommerceweb.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddressDTO {

    private Long id;

    private String fullName;

    private String phone;

    private String postCode;

    private String addressDetails;

    //type of address is home(0) or work(1) place
    private Integer type;

    private Integer isDefault;

    private Date createDate;

    private Date modifyDate;

}
