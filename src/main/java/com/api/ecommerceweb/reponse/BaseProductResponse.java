package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BaseProductResponse implements Serializable {

    private Long id;

    private String name;

    private Double standardPrice;

    private Double salesPrice;

    private ShopInfoResponse shop;


}
