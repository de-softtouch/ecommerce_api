package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ShopRequest {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private AddressRequest address;
}
