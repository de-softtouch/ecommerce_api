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

    @OneToMany(mappedBy = "product")
    @OrderBy(value = "pos ASC ")
    private List<ProductImage> images = new ArrayList<>();

    // set default brand is 0 (no brand)
    @ManyToOne
    private Brand brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
//    @JoinTable(name = "PRODUCT_VARIATIONS", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "variation_id"))
    private Set<Variation> variations = new HashSet<>();

    private Double standardPrice;

    private Double salesPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "users_like_products", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<User> usersLike = new HashSet<>();

    @Column(columnDefinition = "tinyint(2) default 1")
    private Integer active;

    @Column(columnDefinition = "tinyint(2) default 0")
    private Integer status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    private String description;

    //
    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    private OrderItem orderItem;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Shipment> shipments = new HashSet<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Color> colors = new HashSet<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Size> sizes = new HashSet<>();


    public void addImage(ProductImage productImage) {
        this.getImages().add(productImage);
        productImage.setProduct(this);
    }

}
