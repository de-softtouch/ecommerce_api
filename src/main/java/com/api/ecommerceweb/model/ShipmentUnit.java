package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "shipment_units")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShipmentUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double standardPrice;

    private Double minWeight;

    private Double minHeight;

    private Double minLength;

    private Double minWidth;

    private String note;

    //
    @OneToOne(mappedBy = "shipmentUnit")
    private Shipment shipment;


}
