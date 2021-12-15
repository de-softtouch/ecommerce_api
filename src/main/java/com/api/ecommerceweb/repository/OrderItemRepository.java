package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Order;
import com.api.ecommerceweb.model.OrderItem;
import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByProduct_Shop(Shop shop);

    Optional<Order> findByIdAndProduct_Shop(Long id, Shop shop);

//    @Query(
//            nativeQuery = true,
//            value = "SELECT COUNT(ot.id) as count FROM order_items ot JOIN products p " +
//                    "ON ot.product_id = p.id " +
//                    "WHERE p.id =7"
//    )

    int countOrderItemByProduct(Product product);
}
