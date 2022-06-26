package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Variant;
import com.api.ecommerceweb.model.VariantOption;
import com.api.ecommerceweb.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductVariantService {

    private final VariantRepository variantRepo;

    public List<Variant> getVariants(Product product) {
        return variantRepo.findByProduct(product);
    }

    public Variant getById(Long id) {
        return variantRepo.findById(id).orElse(null);
    }

    public boolean existByIdAndProduct(Long id, Product product) {
        return variantRepo.existsByIdAndProduct(id, product);
    }

    public Variant getByIdAndProduct(Long id, Product product) {
        return variantRepo.getByIdAndProduct(id, product).orElse(null);
    }

    public Variant save(Variant variant) {
        return variantRepo.saveAndFlush(variant);
    }

    public boolean existByOption(VariantOption variantOption) {
        return variantRepo.existsByOptions(variantOption);
    }

    public Variant getByNameAndProduct(String name, Product product) {
        return variantRepo.getByNameAndProduct(name,product).orElse(null);
    }
}
