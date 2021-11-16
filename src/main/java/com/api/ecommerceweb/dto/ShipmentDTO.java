package com.api.ecommerceweb.dto;

import lombok.Data;

@Data
public class ShipmentDTO {

    private Long id;

    private int expectedTime;

    private ShipmentUnitDTO shipmentUnit;


}
