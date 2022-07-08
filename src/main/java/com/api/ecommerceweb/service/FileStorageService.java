package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.FileUpload;
import com.api.ecommerceweb.model.User;
import com.api.ecommerceweb.property.FileUploadProperties;
import com.api.ecommerceweb.reponse.FileUploadResponse;
import com.api.ecommerceweb.repository.FileRepository;
import com.api.ecommerceweb.utils.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class FileStorageService {

    private final FileRepository fileUploadRepo;
    private Path rootPath;

    public FileStorageService(FileRepository fileUploadRepo, FileUploadProperties fileUploadProperties) {
        this.fileUploadRepo = fileUploadRepo;
        try {
            //create upload dir
            Path path = Paths.get(fileUploadProperties.getUploadDir()).normalize().toAbsolutePath();
            this.rootPath = Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
            log.info("Could not create upload dir - {}", e.getMessage());
        }
    }

    public Path createSubDir(String name) {
        try {
            Path path = this.rootPath.resolve(name);
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            e.printStackTrace();
            log.info("Could not create sub upload dir - {}", e.getMessage());
            return null;
        }
    }

    public File getFileFromStorage(String fileName) throws FileNotFoundException {
        File rootDir = this.rootPath.toFile();
        File found = fileFromStorageRecursive(rootDir, fileName);
        if (found != null) {
            return found;
        }
        throw new FileNotFoundException("Could not find file with name: " + fileName);
    }

    public FileUpload getFileFromDb(Long id) {
        return fileUploadRepo.findById(id).orElse(null);
    }


    //lay ra file da luu trong o dia cua server
    File fileFromStorageRecursive(File file, String name) {
        if (file.isDirectory() && file.listFiles().length > 0) {
            for (File f :
                    file.listFiles()) {
                File child = fileFromStorageRecursive(f, name);
                if (child != null)
                    return child;
            }
            return null;
        } else if (equalName(name, file)) {
            return file;
        } else {
            return null;
        }

    }

    boolean equalName(String name, File file) {
        String n = file.getName().split("\\.")[0];
        return name.equals(n);
    }


    public FileUploadResponse saveImage(MultipartFile file) throws FileUploadException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (file != null && ValidationUtil.isImage(file)) {
            String contentType = file.getContentType();
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String[] split = originalFilename.split("\\.");
            String modifiedName = split[0] + "-" + System.currentTimeMillis();
            //file upload
            FileUpload fileUpload = new FileUpload();
            fileUpload.setName(modifiedName);
            fileUpload.setOriginalName(originalFilename);
            fileUpload.setType(contentType);
            fileUpload.setSize(file.getSize());
            fileUpload.setExt(split[1]);

            try {
                Path path = createDir(user.getEmail());
                Files.copy(file.getInputStream(), path.resolve(modifiedName + "." + split[1]), StandardCopyOption.REPLACE_EXISTING);
                fileUpload = fileUploadRepo.saveAndFlush(fileUpload);
            } catch (IOException e) {
                throw new FileUploadException(e.getMessage(), e.getCause());
            }
            FileUploadResponse fileUploadResponse = new FileUploadResponse();
            fileUploadResponse.setName(modifiedName);
            fileUploadResponse.setType(contentType);
            fileUploadResponse.setId(fileUpload.getId());
            return fileUploadResponse;
        }
        throw new FileUploadException("File is not valid");

    }

    public Path createDir(String dirName) throws FileUploadException {
        try {
            Path path = Files.createDirectories(this.rootPath.resolve(dirName));
            return path;

        } catch (IOException e) {
            throw new FileUploadException("Could not create dir " + dirName, e.getCause());
        }
    }

    public FileUpload getFileUpload(String name) {
        return fileUploadRepo.findByName(name).orElse(null);
    }


    public FileUpload getById(Long id) {
        return fileUploadRepo.findById(id).orElse(null);
    }

    public FileUpload getByName(String name) {
        return fileUploadRepo.findByName(name).orElse(null);
    }
}
