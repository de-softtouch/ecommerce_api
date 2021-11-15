package com.api.ecommerceweb.model;

import com.api.ecommerceweb.enumm.EPaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "payment_methods")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "payment")
    private EPaymentMethod EPaymentMethod;

    private int active;

}
