package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.request.BrandRequest;
import com.api.ecommerceweb.request.ProductRequest;
import com.api.ecommerceweb.request.ShopRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerHelper {

    private final com.api.ecommerceweb.service.SellerService sellerService;

    public ResponseEntity<?> getAllProducts() {
        return sellerService.getAllProducts();
    }

    public ResponseEntity<?> updateProduct(ProductRequest productRequest) {
        return sellerService.updateProduct(productRequest);
    }

    public ResponseEntity<?> saveBrand(BrandRequest brandRequest) {
        return sellerService.saveBrand(brandRequest);
    }

    public ResponseEntity<?> getAllBrands() {
        return sellerService.getAllBrands();
    }

    public ResponseEntity<?> getProduct(Long id) {
        return sellerService.getProduct(id);
    }

    public ResponseEntity<?> deleteProduct(Long id) {
        return sellerService.deleteProduct(id);
    }

    public ResponseEntity<?> getOrders() {
//        return sellerHelper.getOrders();
        return sellerService.getOrders2();
    }

    public ResponseEntity<?> updateOrder(Long id, Integer status) {
        return sellerService.updateOrder(id, status);
    }

    public ResponseEntity<?> getFeedbacks() {
        return sellerService.getFeedbacks();
    }

    public ResponseEntity<?> getOrder(Long id) {
        return sellerService.getOrder(id);
    }

    public ResponseEntity<?> getShopDetail() {
        Object shopDetail = sellerService.getShopDetail();
        return ResponseEntity.ok(shopDetail);
    }

    public ResponseEntity<?> updateShop(ShopRequest shopRequest) {
        sellerService.updateShop(shopRequest);
        return ResponseEntity.ok("Update shop success");
    }
}
