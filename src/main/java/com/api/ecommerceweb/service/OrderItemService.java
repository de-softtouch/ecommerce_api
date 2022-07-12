package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.OrderItem;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository repo;

    public void save(OrderItem item) {

        repo.save(item);
    }

    public List<Map<String, Object>> countOrderItemStatusTypes(Long shopOwnerId) {
        return repo.countOrderItemStatusTypes(shopOwnerId);
    }

    public List<OrderItem> getOrderItems(Long orderId, Shop shop) {
        return repo.findAllByOrder_IdAndProduct_Shop(orderId, shop);
    }
}
