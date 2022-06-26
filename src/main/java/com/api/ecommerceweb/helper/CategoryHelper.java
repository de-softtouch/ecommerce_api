package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.dto.CategoryDTO;
import com.api.ecommerceweb.mapper.CategoryMapper;
import com.api.ecommerceweb.model.Category;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component("CategoryHelper")
@RequiredArgsConstructor
@Slf4j
public class CategoryHelper {

    private final CategoryService categoryService;

    public ResponseEntity<?> getCategories() {
        //get categoryDTOS
        List<Category> categories = categoryService.findAllByParent(null);
        List<CategoryDTO> categoryDTOS =
                categories.stream().map(CategoryMapper::toCategoryDTO).collect(Collectors.toList());
        for (int i = 0; i < categories.size(); i++) {
            Set<CategoryDTO> rs = categoryDTOS.get(i).getChildren();
            categoryService.findNestedChildren(categories.get(i), rs);

        }
        BaseResponseBody resp = BaseResponseBody.success(categoryDTOS, "Get categories success!", null);
        return ResponseEntity.ok(resp);
    }

    public ResponseEntity<?> getNestedCategory(long id) {
        Category category = categoryService.getCategory(id);
        if (category == null)
            return ResponseEntity.badRequest().body(BaseResponseBody.notFound("Can not category with id: " + id));
        CategoryDTO categoryDTO = CategoryMapper.toCategoryDTO(category);
        categoryService.findNestedChildren(category, categoryDTO.getChildren());
        return ResponseEntity.ok(BaseResponseBody.success(categoryDTO, null, null));
    }
}
