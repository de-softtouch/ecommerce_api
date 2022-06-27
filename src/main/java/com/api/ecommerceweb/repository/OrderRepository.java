package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Order;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//    List<Order> findAllByShop_Owners(User user);
    //    Optional<Order> findByIdAndShop_Owners(Long id, User user);
//v2

    List<Order> findAllByUser(User user);

    Optional<Order> findByIdAndUser(Long id, User user);


    @Query(
            nativeQuery = true,
            value = "SELECT * FROM orders o JOIN users u ON o.user_id = u.id " +
                    "WHERE " +
                    "(:status IS NULL OR o.status = :status) " +
//                    "AND u.id = :userId " +
                    "AND o.active = 1 "
    )
    List<Order> getOrdersNative(@Param("status") Integer status, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM orders o \n" +
                    "JOIN order_items oi ON o.id = oi.order_id \n" +
                    "JOIN products p ON oi.product_id = p.id \n" +
                    "JOIN shops s ON s.id = p.shop_id\n" +
                    "JOIN users u ON s.id = u.shop_id\n" +
                    "WHERE u.id = :sellerId AND o.id = :orderId"
    )
    Optional<Order> getShopOrderNative(@Param("sellerId") Long sellerId, @Param("orderId") Long orderId);


    List<Order> findDistinctByItemsProductShop(Shop shop);


    @Query(
            nativeQuery = true,
            value = "SELECT * FROM orders o JOIN users u ON o.user_id = u.id\n" +
                    "JOIN order_items oi ON o.id = oi.id \n" +
                    "WHERE u.id =:userId\n" +
                    "AND (:status IS NULL OR o.status = :status)\n" +
                    "AND (:status IS NULL OR oi.status = :status)"
    )
    List<Order> getUserOrdersNative(@Param("status") Integer status, @Param("userId") Long userId);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM orders o JOIN order_items oi ON o.id = oi.order_id\n" +
                    "JOIN products p ON p.id = oi.product_id\n" +
                    "JOIN shops s ON s.id = p.shop_id\n" +
                    "JOIN users u ON u.shop_id = s.id\n" +
                    "WHERE u.id = :userId\n" +
                    "AND (:status IS NULL OR o.status = :status)\n" +
                    "AND (:status IS NULL OR oi.status = :status)\n" +
                    "AND p.shop_id = s.id AND oi.product_id = p.id"
    )
    List<Order> getShopOrders(@Param("userId") Long shopOwnerId, @Param("status") Integer status);
}
