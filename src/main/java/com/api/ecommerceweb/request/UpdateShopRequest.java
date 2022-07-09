package com.api.ecommerceweb.request;

import lombok.Data;

@Data
public class UpdateShopRequest {

    private String name;

    private String banner;

    private String description;

    private String avt;

    private Integer status =1;
}
