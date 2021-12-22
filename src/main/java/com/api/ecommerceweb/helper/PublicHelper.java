package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.reponse.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PublicHelper {

    private final com.api.ecommerceweb.service.PublicService publicService;

    public ResponseEntity<?> getNewestProducts() {
        return publicService.getNewestProducts();
    }


    public ResponseEntity<?> getProducts(Map<String, String> params) {
        return publicService.getProducts(params);
    }

    public ResponseEntity<?> getCategories() {
        List<CategoryResponse> rs = publicService.getCategories();
        return ResponseEntity.ok(rs);
    }
}
