package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String primaryImg;

    // set default brand is 0 (no brand)
    @ManyToOne
    private Brand brand;

    @ManyToMany
    @JoinTable(name = "PRODUCT_VARIATIONS", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "variation_id"))
    private Set<Variation> variations = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "users_like_products", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<User> usersLike = new HashSet<>();

    @Column(columnDefinition = "tinyint(3) default 0")
    private Integer active;

    @Column(columnDefinition = "tinyint(3) default 0")
    private Integer status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    //
    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private OrderItem orderItem;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Shipment> shipments = new HashSet<>();
}
