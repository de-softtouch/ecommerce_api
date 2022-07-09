package com.api.ecommerceweb.controller.publicc;

import com.api.ecommerceweb.helper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {


    private final UserHelper userHelper;
    private final ProductHelper productHelper;
    private final CategoryHelper categoryHelper;
    private final BrandHelper brandHelper;
    private final FileUploadHelper fileStorageHelper;
    private final FeedbackHelper feedbackHelper;
    private final ShopHelper shopHelper;

    @GetMapping("/categories")
    public ResponseEntity<?> findAllNestedCategories() {
        return categoryHelper.getCategories();
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<?> findNestedCategory(@PathVariable long id) {
        return categoryHelper.getNestedCategory(id);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> findBrands() {
        return brandHelper.getAllBrands();
    }

    @GetMapping("/products/newest")
    public ResponseEntity<?> getNewestProducts() {
        return productHelper.getNewestProducts();
    }

    //    search product
    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam Map<String, String> params) {
        return productHelper.getProducts(params);
    }

    @GetMapping("/product/purchase-quantity")
    public ResponseEntity<?> getPurchaseQuantities(@RequestParam(value = "modelId", required = false) Long modelId,
                                                   @RequestParam("productId") Long productId,
                                                   @RequestParam("shopId") Long shopId,
                                                   @RequestParam("quantity") Integer quantity
    ) {
        return productHelper.getPurchaseQuantities(modelId, productId, shopId, quantity);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productHelper.getProductDetail(id);
    }

    @GetMapping("/products/{id}/feedbacks")
    public ResponseEntity<?> getProductFeedBacks(@PathVariable Long id) {
        return feedbackHelper.getFeedbacks(id);
    }

    @PostMapping
    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return categoryHelper.getCategories();
    }

    /*
   check input field like 'email','phone','username'
     */
    @GetMapping("/validation")
    public ResponseEntity<?> validationInput(
            @RequestParam("input") String input,
            @RequestParam("value") String value
    ) {
        return userHelper.validationInputField(input, value);
    }

    @GetMapping("/files/{name}")
    public ResponseEntity<?> getFileByName(@PathVariable("name") String name) {
        return fileStorageHelper.getImage(name);
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<?> getShopDetail(@PathVariable Long id) {
        return shopHelper.getShopDetail(id);
    }


}
