package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.ProductDTO;
import com.api.ecommerceweb.model.Product;

public class ProductMapper {

    public static ProductDTO toProductDTO(Product product) {
        ProductDTO p = new ProductDTO();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setCategory(CategoryMapper.toCategoryDTO(product.getCategory()));
        p.setBrand(BrandMapper.toBrandDTO(product.getBrand()));
        p.setCreateDate(product.getCreateDate());
        return p;
    }
}
