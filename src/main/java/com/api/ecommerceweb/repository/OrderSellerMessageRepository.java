package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.OrderSellerMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSellerMessageRepository extends JpaRepository<OrderSellerMessage, Long> {
}
