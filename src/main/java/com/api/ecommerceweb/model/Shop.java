package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shops")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String banner;

    private String description;
    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private Set<User> owners = new HashSet<>();
    //
    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();




}
