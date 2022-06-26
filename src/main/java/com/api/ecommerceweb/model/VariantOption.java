package com.api.ecommerceweb.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name = "variant_option")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VariantOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private Integer status = 1;
    //
    @ManyToMany(mappedBy = "variantOptions")
    private Set<ProductModel> models = new TreeSet<>();




}
