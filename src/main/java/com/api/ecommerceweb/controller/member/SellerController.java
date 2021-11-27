package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.request.BrandRequest;
import com.api.ecommerceweb.request.ProductRequest;
import com.api.ecommerceweb.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return sellerService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        return sellerService.getProduct(id);
    }

    @PostMapping("/products")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest productRequest) {
        return sellerService.updateProduct(productRequest);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        return sellerService.deleteProduct(id);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        return sellerService.getAllBrands();
    }

    @PostMapping("/brands")
    public ResponseEntity<?> saveBrand(@RequestBody @Valid BrandRequest brandRequest) {
        return sellerService.saveBrand(brandRequest);
    }


}
