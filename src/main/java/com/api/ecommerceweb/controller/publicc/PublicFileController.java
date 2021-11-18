package com.api.ecommerceweb.controller.publicc;

import com.api.ecommerceweb.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/public/files")
@RequiredArgsConstructor
public class PublicFileController {

    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {
        return fileStorageService.uploadFile(multipartFile);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> downLoadFile(@PathVariable("fileName") String fileName, HttpServletRequest httpServletRequest) {
        return fileStorageService.downLoadFile(fileName, httpServletRequest);
    }
}
