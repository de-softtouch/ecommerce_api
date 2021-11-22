package com.api.ecommerceweb.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CategoryRequest {

    private Long id;

    @NotBlank
    @Size(min = 4, max = 30)
    private String name;

    private Long parentId;

    private Integer pos;


}
