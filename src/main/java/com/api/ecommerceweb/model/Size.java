package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sizes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String size;

    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    //
    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    private List<Variation> variations = new ArrayList<>();
}
