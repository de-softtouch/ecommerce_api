package com.api.ecommerceweb.controller.publicc;

import com.api.ecommerceweb.service.PublicService;
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

    private final PublicService publicService;

    @GetMapping("/products/newest")
    public ResponseEntity<?> getNewestProducts() {
        return publicService.getNewestProducts();
    }


    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam Map<String, String> params) {
        return publicService.getProducts(params);
    }


    @Data
    public static class Param {
        private int limit = 50;
        private int name;
    }

}
