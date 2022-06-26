package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.File;
import com.api.ecommerceweb.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileRepository fileRepo;

    public File getFile(String name){
        return fileRepo.findByName(name).orElse(null);
    }

    public File getFileById(Long id){
        return fileRepo.findById(id).orElse(null);
    }
}
