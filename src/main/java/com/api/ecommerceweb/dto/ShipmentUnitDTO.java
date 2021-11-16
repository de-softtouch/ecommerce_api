package com.api.ecommerceweb.dto;

import lombok.Data;

@Data
public class ShipmentUnitDTO {

    private Long id;

    private String name;

    private Double standardPrice;

    private Double minWeight;

    private Double minHeight;

    private Double minLength;

    private Double minWidth;

    private String note;

}
