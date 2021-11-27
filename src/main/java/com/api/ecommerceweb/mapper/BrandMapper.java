package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.BrandDTO;
import com.api.ecommerceweb.model.Brand;

public class BrandMapper {

    public static BrandDTO toBrandDTO(Brand brand) {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setId(brand.getId());
        brandDTO.setName(brand.getName());
        return brandDTO;
    }
}
