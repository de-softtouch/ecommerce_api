package com.api.ecommerceweb.dto;

import com.api.ecommerceweb.model.Color;
import com.api.ecommerceweb.model.OrderItem;
import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Size;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * variation of product
 */
@Data
public class VariationDTO {

    private Long id;

    private Double price;

    private Integer qty;

    private Size size;

    private Color color;

}
