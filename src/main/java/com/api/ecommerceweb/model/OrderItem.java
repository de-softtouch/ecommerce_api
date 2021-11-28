package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qty", columnDefinition = "int default 1")
    private Integer qty;

    @OneToOne
    private Variation variation;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToOne
    private Product product;

    private String message;

    //
    @OneToMany(mappedBy = "orderItem",fetch = FetchType.LAZY)
    private List<Feedback> feedbacks = new ArrayList<>();

}
