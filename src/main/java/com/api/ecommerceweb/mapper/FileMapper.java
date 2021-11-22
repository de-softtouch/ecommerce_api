package com.api.ecommerceweb.mapper;

import com.api.ecommerceweb.dto.FileDTO;
import com.api.ecommerceweb.model.File;

public class FileMapper {

    public static FileDTO toFileDTO(File file) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(file.getId());
        fileDTO.setName(file.getName());
        fileDTO.setSize(file.getSize());
        fileDTO.setType(file.getType());
        return fileDTO;
    }
}
