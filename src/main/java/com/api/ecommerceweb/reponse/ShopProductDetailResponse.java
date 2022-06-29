package com.api.ecommerceweb.reponse;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ShopProductDetailResponse  implements Serializable {

    private Long id;

    private String name;

    private Double minPrice;

    private Double maxPrice;

    private Double salesPrice;

    private Double standardPrice;

    private Integer stock;

    private Double price;

    private Double priceBeforeDiscount;

    private String coverImage;

    private Integer solid;

    private ShopInfoResponse shop;

    private List<ProductModelResponse> models = new LinkedList<>();

    private List<ShopVariantResponse> variants = new LinkedList<>();

    private List<ProductImageResponse> images = new LinkedList<>();

    private CategoryResponse category;

    private BrandResponse brand;

    private String description;

    private Integer active;

    private Integer status;
}
