package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByCreateDate();

    @Query(
            nativeQuery = true,
            value = "SELECT p.* FROM products p " +
                    "LEFT JOIN categories c ON p.category_id = c.id " +
                    "LEFT JOIN brands b ON p.brand_id = b.id " +
                    "WHERE (?1 IS NULL OR p.name LIKE ?1) " +
                    "AND (?2 IS NULL OR c.name LIKE ?2) " +
                    "AND (?3 IS NULL OR b.name LIKE ?3) " +
                    "AND (?4 IS NULL OR p.min_price >= ?4) " +
                    " AND (?5 IS NULL OR p.max_price <= ?5) "
    )
    List<Product> search(String name, String category, String brand, Double minPrice, Double doubleMaxPrice, Pageable pageable);


    //get products in shop
    @Query(
            nativeQuery = true,
            value = "SELECT p.* FROM products p \n" +
                    "JOIN shops s \n" +
                    "ON p.shop_id = s.id\n" +
                    "WHERE p.status =:status\n" +
                    "AND s.id = :shopId"
    )
    List<Product> getProductsInShop(@Param("shopId") Long shopId, @Param("status") Integer status, Pageable pageable);

    boolean existsByShop(Shop shop);
}
