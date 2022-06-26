package com.api.ecommerceweb.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariationResponse {

    private Long id;

    private Double standardPrice;

    private Double salesPrice;

    private Integer qty;

    private ColorResponse color;

    private SizeResponse size;


}
