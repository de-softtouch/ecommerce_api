package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * variation of product
 */
@Entity
@Table(name = "variations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Variation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private Integer qty;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    //
    @ManyToMany(mappedBy = "variations", fetch = FetchType.LAZY)
    private Set<Product> products = new HashSet<>();

    @OneToOne(mappedBy = "variation", fetch = FetchType.LAZY)
    private OrderItem orderItem;
}
