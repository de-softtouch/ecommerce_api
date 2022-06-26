package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.OrderItem;
import com.api.ecommerceweb.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository repo;

    public void save(OrderItem item){
         repo.save(item);
    }
}
