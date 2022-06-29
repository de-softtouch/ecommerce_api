package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String name;

    private Double minPrice;

    private Double maxPrice;

    private Double standardPrice;

    private Double salesPrice;

    private Integer stock;

    private String coverImage;

    private Integer solid;

    private ShopInfoResponse shop;

    private Date createDate;

    private Date updateDate;

    private List<ProductImageResponse> images = new LinkedList<>();

    private List<ProductModelResponse> models = new LinkedList<>();

}
