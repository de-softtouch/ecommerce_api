package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.request.BrandRequest;
import com.api.ecommerceweb.request.ProductRequest;
import com.api.ecommerceweb.helper.SellerHelper;
import com.api.ecommerceweb.request.ShopRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerHelper sellerHelper;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return sellerHelper.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        return sellerHelper.getProduct(id);
    }

    @PostMapping("/products")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest productRequest) {
        return sellerHelper.updateProduct(productRequest);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        return sellerHelper.deleteProduct(id);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        return sellerHelper.getAllBrands();
    }

    @PostMapping("/brands")
    public ResponseEntity<?> saveBrand(@RequestBody @Valid BrandRequest brandRequest) {
        return sellerHelper.saveBrand(brandRequest);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrders() {
        return sellerHelper.getOrders();
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Long id){
        return sellerHelper.getOrder(id);

    }
    @PostMapping("/orders/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id, @RequestParam Integer status) {
        return sellerHelper.updateOrder(id, status);
    }

    @GetMapping("/feedbacks")
    public ResponseEntity<?> getFeedbacks() {
        return sellerHelper.getFeedbacks();
    }


    @GetMapping("/users/shops")
    public ResponseEntity<?> getShopDetail() {
        return sellerHelper.getShopDetail();
    }

    @PostMapping("/users/shops")
    public ResponseEntity<?> updateShop(@RequestBody ShopRequest shopRequest){
        return sellerHelper.updateShop(shopRequest);
    }
}
