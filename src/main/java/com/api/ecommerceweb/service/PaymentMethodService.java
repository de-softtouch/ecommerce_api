package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.PaymentMethod;
import com.api.ecommerceweb.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository repo;

    public PaymentMethod getPaymentMethod(Long id) {
        return repo.findById(id).orElse(null);
    }

}
