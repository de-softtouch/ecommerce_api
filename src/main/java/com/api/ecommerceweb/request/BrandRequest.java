package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BrandRequest {

    private Long id;

    @NotBlank
    private String name;
}
