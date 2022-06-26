package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.ProductModel;
import com.api.ecommerceweb.model.Variant;
import com.api.ecommerceweb.model.VariantOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariantOptionRepository extends JpaRepository<VariantOption, Long> {

    Optional<VariantOption> findByIdAndAndModels(Long id, ProductModel model);

    Optional<VariantOption> findByIdAndVariant(Long id, Variant variant);

    Optional<VariantOption> findByIdAndVariant_Product(Long id, Product product);

    Optional<VariantOption> findByNameAndVariant_Product(String name, Product product);

}
