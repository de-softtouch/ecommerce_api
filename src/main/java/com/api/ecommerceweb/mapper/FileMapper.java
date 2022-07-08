package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.FileDTO;
import com.api.ecommerceweb.model.FileUpload;

public class FileMapper {

    public static FileDTO toFileDTO(FileUpload fileUpload) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(fileUpload.getId());
        fileDTO.setName(fileUpload.getName());
        fileDTO.setSize(fileUpload.getSize());
        fileDTO.setType(fileUpload.getType());
        return fileDTO;
    }
}
