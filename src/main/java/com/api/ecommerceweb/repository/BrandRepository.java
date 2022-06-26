package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    Brand getByName(String name);

    Optional<Brand> findByName(String brandReqName);
}
