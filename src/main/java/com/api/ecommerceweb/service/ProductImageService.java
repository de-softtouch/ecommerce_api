package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.ProductImage;
import com.api.ecommerceweb.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository repo;

    public void save(ProductImage image){
        repo.save(image);
    }

    public List<ProductImage> getProductImages(Product product, Integer i) {
return         repo.findByProductAndStatus(product,i);
    }
}
