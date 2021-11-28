package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Order;
import com.api.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByShop_Owners(User user);

    List<Order> findAllByUser(User user);

    Optional<Order> findByIdAndShop_Owners(Long id, User user);

    Optional<Order> findByIdAndUser(Long id, User user);
}
