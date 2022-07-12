package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Feedback;
import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedBackService {

    private final FeedbackRepository repo;

    public List<Feedback> getFeedbacks(Product product) {
        return repo.findByProduct(product);
    }

    public List<Feedback> getShopFeedbacks(Shop shop) {
        return repo.findAllByProduct_ShopOrderByCreateDateDesc(shop);

    }
}
