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
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String postCode;

    @Column(nullable = false)
    private String addressDetails;

    //type of address is home(0) or work(1) place
    @Column(name = "type", columnDefinition = "tinyint(2) default 0")
    private Integer type;

    @Column(name = "is_default", columnDefinition = "tinyint(2) default 0")
    private Integer isDefault;

    @Column(name = "status", columnDefinition = "tinyint(2) default 1", nullable = false)
    private int status = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    //
    @OneToMany(mappedBy = "address")
    private List<Order> orders = new ArrayList<>();
}
