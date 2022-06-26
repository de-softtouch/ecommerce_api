package com.api.ecommerceweb.service;

import com.api.ecommerceweb.dto.CategoryDTO;
import com.api.ecommerceweb.mapper.CategoryMapper;
import com.api.ecommerceweb.model.Category;
import com.api.ecommerceweb.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public List<Category> findAllByParent(Category category) {
        return categoryRepo.findAllByParentOrderByPosAscNameAsc(category);
    }

    public Category getCategory(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public void findNestedChildren(Category parent, Set<CategoryDTO> rs) {
        List<Category> children = categoryRepo.findAllByParentOrderByPosAscNameAsc(parent);
        for (int i = 0; i < children.size(); i++) {
            Category c = children.get(i);
            CategoryDTO categoryDTO = CategoryMapper.toCategoryDTO(c);
            rs.add(categoryDTO);
            if (c.getParent() != null && !categoryRepo.findAllByParentOrderByPosAscNameAsc(c).isEmpty()) {
                findNestedChildren(c, categoryDTO.getChildren());
            }
        }
    }


    public Category getByName(String uncategory) {
        return categoryRepo.findByName(uncategory).orElse(null);
    }
}
