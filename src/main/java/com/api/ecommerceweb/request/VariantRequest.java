package com.api.ecommerceweb.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VariantRequest {

    private Long id;

    private String name;

    private List<VariantOptionRequest> options = new ArrayList<>();

    private Integer status = 1;
}
