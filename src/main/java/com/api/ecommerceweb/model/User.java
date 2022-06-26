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

    @Column(unique = true,nullable = false)
    private String email;

    @Column(unique = true,nullable = false)
    private String phone;

    private String password;

    private String fullName;

    private String profileImg;

    @Column(unique = true)
    private String username;

    private Integer gender;

    private String verificationCode;

    private Date dob;

    @Column(columnDefinition = "tinyint(2) default 0")
    private Integer active;

    private Integer status =1;

    @Column(name = "auth_provider")
    @Enumerated(EnumType.ORDINAL)
    private AuthenticateProvider authProvider;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createDate;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shop shop;

    //
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @OrderBy("createDate ASC")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Feedback> feedbacks = new HashSet<>();

    @ManyToMany(mappedBy = "usersLike", fetch = FetchType.LAZY)
    private Set<Product> likedProducts = new HashSet<>();

    public User(Long id, String username, String fullName, String phone, String email, String password, String verificationCode, String avt, int active, int status, Date createDate, Date updateDate, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.profileImg = avt;
        this.active = active;
        this.status = status;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.roles = roles;
    }


}
