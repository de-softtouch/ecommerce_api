package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.ProductModel;
import com.api.ecommerceweb.model.Variant;
import com.api.ecommerceweb.model.VariantOption;
import com.api.ecommerceweb.repository.VariantOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VariantOptionService {

    private final VariantOptionRepository variantOptionRepository;


    public boolean existById(Long id) {
        return variantOptionRepository.existsById(id);
    }

    public VariantOption getById(Long id) {
        return variantOptionRepository.findById(id).orElse(null);
    }

    public VariantOption save(VariantOption variantOption) {
        return variantOptionRepository.save(variantOption);
    }

    public VariantOption getByIdProductModel(Long id, ProductModel productModel) {
        return variantOptionRepository.findByIdAndAndModels(id, productModel).orElse(null);
    }

    public VariantOption getByIdAndVariant(Long id, Variant variant) {
        return variantOptionRepository.findByIdAndVariant(id, variant).orElse(null);
    }

    public VariantOption getByIdAndProduct(Long id, Product product) {
        return variantOptionRepository.findByIdAndVariant_Product(id, product).orElse(null);
    }

    public VariantOption getByNameAndProduct(String name, Product product) {
        return variantOptionRepository.findByNameAndVariant_Product(name, product).orElse(null);
    }
}
