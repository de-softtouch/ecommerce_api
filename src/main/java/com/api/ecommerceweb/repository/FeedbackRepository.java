package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Feedback;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    List<Feedback> findAllByUser(User user);

    List<Feedback> findAllByProductShopOrderByCreateDate(Shop shop);
}
