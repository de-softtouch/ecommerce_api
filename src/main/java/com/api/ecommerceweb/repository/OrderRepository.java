package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Order;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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


//    @Query(
//            nativeQuery = true,
//            value = "SELECT o.* FROM orders o JOIN order_items oi ON o.id = oi.id" +
//                    " JOIN products p ON p.id = oi.product_id" +
//                    " JOIN shops s ON s.id = p.shop_id WHERE s.id =?1"
//    )
//    List<Order> findByOrderItemsProductShop(Long shopId);

    List<Order> findDistinctByOrderItemsProductShop(Shop shop);

//    Optional<Order> findByIdAndShops(Long id, Shop shop);
}
