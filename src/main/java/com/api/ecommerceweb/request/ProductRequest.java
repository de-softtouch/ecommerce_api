package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public class ProductRequest {

    private Long id;

    @NotBlank
    @Size(min = 10, max = 100)
    private String name;

    private Long primaryImgId;

    private Set<Long> images = new TreeSet<>();

    private BrandRequest brand;

    private Long categoryId;

    private Double standardPrice;

    private Double salesPrice;

    private List<VariationRequest> variations = new ArrayList<>();

    private Integer status = 0;

    private Integer active = 1;

}
