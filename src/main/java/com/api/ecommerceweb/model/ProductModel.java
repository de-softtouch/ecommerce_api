package com.api.ecommerceweb.model;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "product_model")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private Double priceBeforeDiscount;

    private Integer stock;

    private Integer maxPurchaseQuantity;

    @ManyToMany
    @JoinTable(
            name = "product_model_variant_option",
            joinColumns = @JoinColumn(name = "product_model_id"),
            inverseJoinColumns = @JoinColumn(name = "variant_option_id")
    )
    private List<VariantOption> variantOptions = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer status;

    @OneToMany(mappedBy = "model")
    private Collection<OrderItem> orderItems = new ArrayList<>();

}
