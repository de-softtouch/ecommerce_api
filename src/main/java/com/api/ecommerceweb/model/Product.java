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

    @ManyToOne
    @JoinColumn(name = "cover_img_id")
    private FileUpload coverImage;

    @ManyToOne
    @JoinColumn(name = "cover_vid_id")
    private FileUpload coverVideo;

    @OneToMany(mappedBy = "product")
    @OrderBy(value = "pos ASC ")
    private List<ProductImage> images = new ArrayList<>();

    // set default brand is 0 (no brand)
    @ManyToOne
    private Brand brand;

    @OneToMany(mappedBy = "product")
    private List<ProductModel> models = new LinkedList<>();

    private Integer stock;

    private Double minPrice;

    private Double maxPrice;

    private Double standardPrice;

    private Double salesPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(name = "users_like_products", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<User> usersLike = new HashSet<>();

    @Column(columnDefinition = "tinyint(10) default 1")
    private Integer active;

    @Column(columnDefinition = "tinyint(10) default 1")
    private Integer status;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;
    //
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<Shipment> shipments = new HashSet<>();

    public void addImage(ProductImage productImage) {
        this.getImages().add(productImage);
        productImage.setProduct(this);
    }

    @OneToMany(mappedBy = "product", targetEntity = Variant.class)
    private List<Variant> variants = new LinkedList<>();


}
