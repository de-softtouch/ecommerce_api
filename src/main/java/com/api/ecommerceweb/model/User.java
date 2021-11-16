package com.api.ecommerceweb.model;

import com.api.ecommerceweb.enumm.AuthenticateProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phone;

    private String password;

    private String fullName;

    private Integer gender;

    private Integer verificationCode;

    @Column(columnDefinition = "tinyint(2) default 0")
    private Integer active;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.ORDINAL)
    private AuthenticateProvider authProvider;

    @ManyToMany
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;

    //
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OrderBy("orderDate ASC")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<>();

    @ManyToMany(mappedBy = "usersLike", fetch = FetchType.LAZY)
    private Set<Product> likedProducts = new HashSet<>();


}
