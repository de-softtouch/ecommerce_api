package com.api.ecommerceweb.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class FileStorageHelper {
    private final com.api.ecommerceweb.service.FileStorageService fileStorageService;

    public ResponseEntity<?> uploadFile(MultipartFile multipartFile) {
        return fileStorageService.uploadFile(multipartFile);
    }

    public ResponseEntity<?> downLoadFile(String fileName, HttpServletRequest httpServletRequest) {
        return fileStorageService.downloadFile(fileName, httpServletRequest);
    }
}
