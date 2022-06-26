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

    @Column(nullable = false)
    private String name;

    private String banner;

    @Column(nullable = false)
    private String description;

    private Integer status;

    private Integer active;
    @Column(nullable = false)

    private String email;
    @Column(nullable = false)
    private String numberPhone;

    private String verificationCode;
    //
    @OneToOne
    private Address address;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private Set<User> owners = new HashSet<>();


}
