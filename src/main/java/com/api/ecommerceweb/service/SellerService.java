package com.api.ecommerceweb.service;

import com.api.ecommerceweb.helper.SellerHelper;
import com.api.ecommerceweb.request.BrandRequest;
import com.api.ecommerceweb.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerHelper sellerHelper;

    public ResponseEntity<?> getAllProducts() {
        return sellerHelper.getAllProducts();
    }

    public ResponseEntity<?> updateProduct(ProductRequest productRequest) {
        return sellerHelper.updateProduct(productRequest);
    }

    public ResponseEntity<?> saveBrand(BrandRequest brandRequest) {
        return sellerHelper.saveBrand(brandRequest);
    }

    public ResponseEntity<?> getAllBrands() {
        return sellerHelper.getAllBrands();
    }

    public ResponseEntity<?> getProduct(Long id) {
        return sellerHelper.getProduct(id);
    }

    public ResponseEntity<?> deleteProduct(Long id) {
        return sellerHelper.deleteProduct(id);
    }

    public ResponseEntity<?> getOrders() {
//        return sellerHelper.getOrders();
        return sellerHelper.getOrders2();
    }

    public ResponseEntity<?> updateOrder(Long id, Integer status) {
        return sellerHelper.updateOrder(id, status);
    }

    public ResponseEntity<?> getFeedbacks() {
        return sellerHelper.getFeedbacks();
    }

    public ResponseEntity<?> getOrder(Long id) {
        return sellerHelper.getOrder(id);
    }
}
