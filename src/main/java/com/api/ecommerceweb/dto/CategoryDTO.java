package com.api.ecommerceweb.dto;

import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryDTO {

    private Long id;

    private String name;

    private Date createDate;

    private Set<CategoryDTO> children = new HashSet<>();

    private FileDTO img;

    private Integer pos;
}
