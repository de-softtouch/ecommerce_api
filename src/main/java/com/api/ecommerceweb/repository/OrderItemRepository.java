package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByProduct_Shop(Shop shop);

    Optional<Order> findByIdAndProduct_Shop(Long id, Shop shop);

    int countOrderItemByProduct(Product product);

    @Query(
            nativeQuery = true,
            value = "SELECT oi.* FROM orders o JOIN order_items oi ON o.id = oi.order_id\n" +
                    "JOIN products p ON p.id = oi.product_id\n" +
                    "JOIN shops s ON s.id = p.shop_id\n" +
                    "JOIN users u ON u.shop_id = s.id\n" +
                    "WHERE u.id = :userId AND oi.product_id = p.id\n" +
                    "AND (:status IS NULL OR o.status = :status)\n" +
                    "AND (:status IS NULL OR oi.status = :status)"
    )
    List<Order> getShopOrderItems(@Param("userId") Long shopOwnerId, @Param("status") Integer status);


    @Query(
            nativeQuery = true,
            value = "SELECT oi.status,COUNT(oi.id) as count FROM orders o JOIN order_items oi ON o.id = oi.order_id\n" +
                    "JOIN products p ON p.id = oi.product_id\n" +
                    "JOIN shops s ON s.id = p.shop_id\n" +
                    "JOIN users u ON u.shop_id = s.id\n" +
                    "WHERE u.id = :userId AND p.shop_id = s.id AND oi.product_id = p.id \n" +
                    "GROUP BY oi.status"
    )
    List<Map<String, Object>> countOrderItemStatusTypes(@Param("userId") Long ownerId);



}
