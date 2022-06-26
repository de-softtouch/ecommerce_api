package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Payment;
import com.api.ecommerceweb.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepo;

    public Payment getById(Long id) {
        return paymentRepo.findById(id).orElse(null);
    }

    public Payment saveFlush(Payment payment) {
        return paymentRepo.saveAndFlush(payment);
    }
}
