package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.CategoryDTO;
import com.api.ecommerceweb.dto.FileDTO;
import com.api.ecommerceweb.model.Category;

public class CategoryMapper {

    public static CategoryDTO toCategoryDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        categoryDTO.setPos(category.getPos());
        if (category.getImage() != null) {
            FileDTO fileDto = new FileDTO();
            fileDto.setName(category.getImage().getName());
            fileDto.setId(category.getImage().getId());
            categoryDTO.setImg(fileDto);
        }
        return categoryDTO;
    }
}
