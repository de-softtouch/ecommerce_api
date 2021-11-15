package com.api.ecommerceweb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "order_seller_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderSellerMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "pos_index", nullable = false)
    private int posIndex;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date orderDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
}
