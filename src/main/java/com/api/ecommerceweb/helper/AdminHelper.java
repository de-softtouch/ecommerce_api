package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminHelper {

    private final com.api.ecommerceweb.service.AdminService adminService;

    public ResponseEntity<?> getCategories() {
        return adminService.getCategories();
    }

    public ResponseEntity<?> update(CategoryRequest categoryRequest, MultipartFile file) {
        return adminService.update(categoryRequest, file);
    }

    public ResponseEntity<?> removeCategory(Long id) {
        return adminService.removeCategory(id);
    }
}
