package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {


    //    List<Category> findAllByParentOrderByNameAscPosAsc(Category category);
    List<Category> findAllByParentOrderByPosAscNameAsc(Category category);


    Optional<Category> findByName(String uncategory);
}
