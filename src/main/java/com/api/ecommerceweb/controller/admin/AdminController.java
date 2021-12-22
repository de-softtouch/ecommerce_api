package com.api.ecommerceweb.controller.admin;

import com.api.ecommerceweb.request.CategoryRequest;
import com.api.ecommerceweb.helper.AdminHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminHelper adminHelper;

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return adminHelper.getCategories();
    }

    @PostMapping(value = "/categories", consumes = "multipart/form-data")
    public ResponseEntity<?> updateCategories(
            @ModelAttribute @Valid CategoryRequest categoryRequest,
            @RequestParam(value = "img", required = false) MultipartFile file) {
        return adminHelper.update(categoryRequest, file);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> removeCategory(@PathVariable("id") Long id) {
        return adminHelper.removeCategory(id);
    }
}