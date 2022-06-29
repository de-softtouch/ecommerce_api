package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.ProductModel;
import com.api.ecommerceweb.model.VariantOption;
import com.api.ecommerceweb.repository.ProductModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductModelService {

    private final ProductModelRepository productModelRepo;

    public ProductModel getById(Long id) {
        return productModelRepo.findById(id).orElse(null);
    }

    public List<ProductModel> getProductModels(Product product, Integer status) {
        return productModelRepo.findByProductAndStatus(product, status);
    }

    public List<ProductModel> getAllByVariantOption(VariantOption variantOption) {
        return productModelRepo.findAllByVariantOptions(variantOption);
    }

    public ProductModel getByIdAndProduct(Long id, Product product) {
        return productModelRepo.findByIdAndProduct(id, product).orElse(null);
    }

    public ProductModel getByNameAndProduct(String name, Product product) {
        return productModelRepo.findByNameAndProduct(name, product).orElse(null);
    }

    public ProductModel save(ProductModel productModel) {
        return productModelRepo.saveAndFlush(productModel);
    }

    public ProductModel findByVariantOptions(VariantOption... variantOptions) {
        return productModelRepo.findByVariantOptions(variantOptions).orElse(null);
    }

    public ProductModel findProductModelNative(Long modelId, Long optionId) {
        return productModelRepo.getProductModelNative(modelId, optionId).orElse(null);
    }

    public boolean existByVariantOption(Long productModelId, VariantOption variantOption) {
        return productModelRepo.existsByIdAndVariantOptions(productModelId, variantOption);
    }

    public ProductModel getPurcharQuantity(Long productId, Long modelId, Long shopId, Integer quantity) {
        return productModelRepo.getPurchaseQuantity(productId, modelId, shopId, quantity).orElse(null);

    }


}
