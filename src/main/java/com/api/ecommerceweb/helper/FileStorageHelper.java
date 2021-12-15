package com.api.ecommerceweb.helper;

import com.api.ecommerceweb.model.File;
import com.api.ecommerceweb.reponse.UploadFileResponse;
import com.api.ecommerceweb.repository.FileRepository;
import com.api.ecommerceweb.utils.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("FileStorageHelper")
@RequiredArgsConstructor
@Slf4j
public class FileStorageHelper {

    private final FileStorageUtil fileStorageUtil;
    private final FileRepository fileRepository;

    public ResponseEntity<?> uploadFile(MultipartFile multipartFile) {
        String savedFileName = fileStorageUtil.storeFile(multipartFile, "HHI");
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/public/files/")
                .path(savedFileName)
                .toUriString();

        File file = new File();
        file.setName(savedFileName);
        file.setType(multipartFile.getContentType());
        file.setSize(multipartFile.getSize());
        fileRepository.save(file);

        return ResponseEntity.ok(
                new UploadFileResponse(savedFileName, fileDownloadUri, multipartFile.getContentType(), multipartFile.getSize()));
    }

    public ResponseEntity<?> downloadFile(String fileName, HttpServletRequest httpServletRequest) {
//        load file as resource
//        Resource resource = fileStorageUtil.loadFile(fileName);
        Resource resource = fileStorageUtil.loadFile(fileName);
        //try to determine file's content type
        String contentType = null;
        try {
            contentType = httpServletRequest.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type - {}", ex.getMessage());
        }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
