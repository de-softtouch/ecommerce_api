package com.api.ecommerceweb.request;

import com.api.ecommerceweb.model.File;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Data
public class ProductRequest {

    private Long id;

    @NotBlank
    @Size(min = 10, max = 100)
    private String name;

    @NotBlank
    private String coverImage;

    private String coverVideo;

    private Set<File> files = new HashSet<>();

    private Set<String> images = new TreeSet<>();

    private BrandRequest brand;

    private Long categoryId;

    private Double standardPrice;

    private Double salesPrice;

    private List<VariationRequest> variations = new ArrayList<>();

    private Integer status = 0;

    private Integer active = 1;

    private String description;

    private String shortDescription;

}
