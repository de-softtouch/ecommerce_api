package com.api.ecommerceweb.reponse;

import com.api.ecommerceweb.model.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {

    private Long id;

    private String name;

    private Double minPrice;

    private Double maxPrice;

    private Double price;

    private Double priceBeforeDiscount;

    private String coverImage;

    private Integer solid;

    private ShopInfoResponse shop;

    private List<ProductModelResponse> models = new LinkedList<>();

    private List<VariantResponse> variants = new LinkedList<>();

    private List<ProductImageResponse> images = new LinkedList<>();

    private CategoryResponse category;

    private BrandResponse brand;

    private String description;
}
