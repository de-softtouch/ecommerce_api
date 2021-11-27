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
import java.util.List;

@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private Long size;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    //
    @OneToOne(mappedBy = "image")
    private Category category;

    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
    private List<ProductImage> products = new ArrayList<>();


}