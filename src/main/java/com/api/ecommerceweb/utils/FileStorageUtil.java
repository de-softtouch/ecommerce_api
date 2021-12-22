package com.api.ecommerceweb.utils;

import com.api.ecommerceweb.exeption.FileStorageException;
import com.api.ecommerceweb.exeption.MyFileNotFoundException;
import com.api.ecommerceweb.property.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component("FileStorageUtil")
@Slf4j
@RequiredArgsConstructor
public class FileStorageUtil {

    private Path rootDir;


    @Autowired
    public FileStorageUtil(FileStorageProperties fileStorageProperties) {
        Path path = Paths.get(fileStorageProperties.getUploadDir()).normalize().toAbsolutePath();
        try {
            //create root directory
            rootDir = Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path createSubDir(String dirName) {
        try {
            Path createdDirPath = Files.createDirectories(this.rootDir.resolve(dirName));
            log.info("Created directory : " + createdDirPath);
            return createdDirPath;
        } catch (IOException e) {
            log.error("Could not create storage directory - {}", e.getMessage());
            throw new FileStorageException("Could not create storage directory", e.getCause());
        }
    }

    public String storeFile(MultipartFile file) throws FileStorageException {
        return store(file, rootDir);
    }


    public String storeFile(MultipartFile file, String folder) throws FileStorageException {
        Path path = createSubDir(folder);
        return store(file, path);

    }


    String store(MultipartFile file, Path dir) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (fileName.contains(".."))
            throw new FileStorageException("File contains invalid characters");
        String[] split = fileName.split("\\.");
        fileName = split[0] + "-" + System.currentTimeMillis() + "." + split[1];
        Path path = dir.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file ", e.getCause());
        }
    }

    public Resource loadFile(String fileName) {
        String found = searchFile2(new File(this.rootDir.toUri()), fileName);
        Resource resource;
        try {
            resource = new FileUrlResource(found);
            if (resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        throw new MyFileNotFoundException("File not found " + fileName);
    }

    public List<File> getFiles(String folder) {
        List<File> rs = new ArrayList<>();
        File file = new File(this.rootDir.resolve(folder).toUri());
        if (!file.exists()) {
            return new ArrayList<>();
        }
        return getFiles(file, rs);
    }

    List<File> getFiles(File file, List<File> rs) {
        if (file.isDirectory()) {
            for (File f :
                    Objects.requireNonNull(file.listFiles())) {
                if (f.isDirectory()) {
                    return getFiles(f, rs);
                }
                rs.add(f);
            }
        } else {
            rs.add(file);
        }
        return rs;
    }

    String searchFile2(File file, String name) {
        if (file.isDirectory()) {
            File[] arr = file.listFiles();
            assert arr != null;
            for (File f : arr) {
                String path = searchFile2(f, name);
                if (path != null)
                    return path;
            }
        } else {
            if (file.getName().equals(name)) {
                return file.getPath();
            }
        }
        throw new MyFileNotFoundException("Not found");

    }


    public boolean isImage(MultipartFile multipartFile) {
        String contentType = multipartFile.getContentType();
        return contentType != null && contentType.split("/")[0].equals("image");
    }


}
