package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Order;
import com.api.ecommerceweb.repository.OrderRepository;
import com.api.ecommerceweb.request.BaseParamRequest;
import com.api.ecommerceweb.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;

    public List<Order> getShopOrders(Map<String, String> params) {
        BaseParamRequest p = new BaseParamRequest(params);
        if (ValidationUtil.isNullOrBlank(params.get("status"))) {
            p.setStatus(null);
        }
        List<Order> orders = orderRepo.getOrdersNative(p.getStatus(), p.toPageRequest());
        return orders;
    }

    public Order getShopOrder(Long orderId, Long shopOwnerId) {
        return orderRepo.getShopOrderNative(shopOwnerId, orderId).orElse(null);
    }

    public Order saveFlush(Order order) {
        return orderRepo.saveAndFlush(order);
    }

    public List<Order> getUserOrders(Long userId, Integer status) {
        return orderRepo.getUserOrdersNative(status, userId);
    }

    public List<Order> getShopOrders(Long ownerId,Integer status){
        return orderRepo.getShopOrders(ownerId,status);
    }
}
