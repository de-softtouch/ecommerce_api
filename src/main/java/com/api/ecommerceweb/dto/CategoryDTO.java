package com.api.ecommerceweb.dto;

import lombok.Data;

@Data
public class CategoryDTO {

    private Long id;

    private String name;

    private CategoryDTO subCategory;

}
