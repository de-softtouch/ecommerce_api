package com.api.ecommerceweb.dto;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ProductDTO {

    private Long id;

    private String name;

    private String primaryImg;

    // set default brand is 0 (no brand)
    private BrandDTO brand;

    private Set<VariationDTO> variations = new HashSet<>();

    private CategoryDTO category;

    private Integer active;

    private Integer status;

    private Date createDate;

    private Date modifyDate;

    private Set<ShipmentDTO> shipments = new HashSet<>();


}
