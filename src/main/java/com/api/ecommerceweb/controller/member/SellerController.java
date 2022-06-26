package com.api.ecommerceweb.controller.member;

import com.api.ecommerceweb.helper.ProductHelper;
import com.api.ecommerceweb.helper.UserHelper;
import com.api.ecommerceweb.request.BrandRequest;
import com.api.ecommerceweb.request.ProductRequest;
import com.api.ecommerceweb.helper.SellerHelper;
import com.api.ecommerceweb.request.ShopRequest;
import com.api.ecommerceweb.request.UpdateShopRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerHelper sellerHelper;
    private final UserHelper userHelper;
    private final ProductHelper productHelper;

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(@RequestParam Map<String, String> params) {
        return productHelper.getProductsInShop(params);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        return sellerHelper.getProduct(id);
    }

    @PostMapping("/products")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid ProductRequest productRequest) {
//        return sellerHelper.updateProduct(productRequest);
        return productHelper.saveProduct(productRequest);
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
    public ResponseEntity<?> getOrders(@RequestParam Map<String, String> params) {
        return sellerHelper.getOrders(params);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Long id) {
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


    @PostMapping("/shops")
    public ResponseEntity<?> updateShop(@RequestBody @Valid UpdateShopRequest request) {
        return userHelper.updateShop(request);
    }
}
