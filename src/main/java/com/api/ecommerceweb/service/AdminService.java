package com.api.ecommerceweb.service;

import com.api.ecommerceweb.helper.AdminHelper;
import com.api.ecommerceweb.request.CategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminHelper adminHelper;

    public ResponseEntity<?> getCategories() {
        return adminHelper.getCategories();
    }

    public ResponseEntity<?> update(CategoryRequest categoryRequest, MultipartFile file) {
        return adminHelper.update(categoryRequest, file);
    }

    public ResponseEntity<?> removeCategory(Long id) {
        return adminHelper.removeCategory(id);
    }
}
