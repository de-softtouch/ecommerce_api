package com.api.ecommerceweb.service;

import com.api.ecommerceweb.helper.FileStorageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileStorageHelper fileStorageHelper;

    public ResponseEntity<?> uploadFile(MultipartFile multipartFile) {
        return fileStorageHelper.uploadFile(multipartFile);
    }

    public ResponseEntity<?> downLoadFile(String fileName, HttpServletRequest httpServletRequest) {
        return fileStorageHelper.downloadFile(fileName, httpServletRequest);
    }
}
