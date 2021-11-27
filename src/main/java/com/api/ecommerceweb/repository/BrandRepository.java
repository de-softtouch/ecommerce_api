package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    Brand getByName(String name);
}
