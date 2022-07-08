package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.FileUpload;
import com.api.ecommerceweb.reponse.BaseResponseBody;
import com.api.ecommerceweb.reponse.FileUploadResponse;
import com.api.ecommerceweb.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.core.io.FileUrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@Component("FileUploadHelper")
@RequiredArgsConstructor
public class FileUploadHelper {
    private final FileStorageService fileStorageService;

    public ResponseEntity<?> uploadFile(MultipartFile file) {
        try {
            FileUploadResponse fileUploadResponse = fileStorageService.saveImage(file);
            return ResponseEntity.ok(BaseResponseBody.success(fileUploadResponse, "Upload file success!", null));
        } catch (FileUploadException e) {
            return ResponseEntity.badRequest().body(BaseResponseBody.badRequest(e.getMessage()));
        }
    }

    public ResponseEntity<?> getImage(String fileName) {
        //check file upload info exist in db
        FileUpload fileUpload = fileStorageService.getFileUpload(fileName);
        if (fileUpload != null) {
            //return file as resource
            try {
                File file = fileStorageService.getFileFromStorage(fileName);
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(new FileUrlResource(file.getPath()));
            } catch (MalformedURLException | FileNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body((e.getMessage()));
            }
        }

        return ResponseEntity.badRequest().body("Could not find file with name : " + fileName);
    }
}
