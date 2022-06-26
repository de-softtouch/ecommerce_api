package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Variant;
import com.api.ecommerceweb.model.VariantOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    List<Variant> findByProduct(Product product);

    boolean existsByIdAndProduct(Long id, Product product);

    Optional<Variant> getByIdAndProduct(Long id, Product product);

    boolean existsByOptions(VariantOption variantOption);

    Optional<Variant> getByOptions(VariantOption variantOption);

    Optional<Variant> getByNameAndProduct(String name, Product product);
}
