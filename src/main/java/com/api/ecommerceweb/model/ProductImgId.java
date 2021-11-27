package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImgId implements Serializable {

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "image_id")
    private Long imageId;


}
