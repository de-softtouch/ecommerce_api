package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String originalName;

    private String type;

    private Long size;

    private String ext;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date create_date;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    //
    @OneToOne(mappedBy = "image")
    private Category category;

    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
    private List<ProductImage> products = new ArrayList<>();

    @OneToMany(mappedBy = "coverImage", fetch = FetchType.LAZY)
    private List<Product> productImageCovers = new ArrayList<>();

    @OneToMany(mappedBy = "coverVideo", fetch = FetchType.LAZY)
    private List<Product> productVideoCovers = new ArrayList<>();


    @ManyToMany(mappedBy = "images",fetch = FetchType.LAZY)
    private List<Variant> variants = new ArrayList<>();
}
