package com.api.ecommerceweb.controller.publicc;

import com.api.ecommerceweb.helper.PublicHelper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final PublicHelper publicHelper;

    @GetMapping("/products/newest")
    public ResponseEntity<?> getNewestProducts() {
        return publicHelper.getNewestProducts();
    }


    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam Map<String, String> params) {
        return publicHelper.getProducts(params);
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories(){
        return publicHelper.getCategories();
    }


    @Data
    public static class Param {
        private int limit = 50;
        private int name;
    }

}
