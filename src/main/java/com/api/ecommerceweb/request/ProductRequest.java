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

    private Long coverImage;

    private String coverVideo;

    private Set<File> files = new HashSet<>();

    private Set<Long> images = new TreeSet<>();

    private BrandRequest brand;

    private Long categoryId;

    private Integer stock;

    private Double minPrice;

    private Double maxPrice;

    private Double standardPrice;

    private Double salesPrice;

    private List<VariantRequest> variants = new LinkedList<>();

    private List<ProductModelRequest> models = new LinkedList<>();

    private Integer status = 1;

    private Integer active = 1;

    private String description;

    private String shortDescription;


    private Set<Long> removedOptions = new HashSet<>();

}
