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


    //    @Query(
//            nativeQuery = true,
//            value = "SELECT p FROM products p " +
//                    "LEFT JOIN categories c ON p.category_id = c.id " +
//                    "LEFT JOIN brands b ON p.brand_id = b.id " +
//                    "WHERE (?1 IS NULL OR p.name LIKE ?1) " +
//                    "AND (?2 IS NULL OR c.name LIKE ?2) " +
//                    "AND (?3 IS NULL OR b.name LIKE ?3) " +
//                    "AND (?5 IS NULL OR p.standard_price >= ?5) " +
//                    " AND (?6 IS NULL OR p.standard_price <= ?6) " +
//                    " ORDER BY p.create_date ?7 " +
//                    " LIMIT ?4 "
//    )
//    List<Product> search(String name, String category, String brand, Integer limit, Double minPrice, Double doubleMaxPrice, String order);
    @Query(
            value =
                    "SELECT p.* FROM products p " +
                            "LEFT JOIN categories c ON p.category_id = c.id " +
                            "LEFT JOIN brands b ON p.brand_id = b.id " +
                            "WHERE (:name IS NULL OR p.name LIKE :name) " +
                            "AND (:category IS NULL OR c.name LIKE :category) " +
                            "AND (:brand IS NULL OR b.name LIKE :brand) " +
                            "AND (:minPrice IS NULL OR p.standard_price >= :minPrice) " +
                            "AND (:maxPrice IS NULL OR p.standard_price <= :maxPrice) ",

            nativeQuery = true
    )
    List<Product> search(@Param("name") String productName,
                         @Param("category") String categoryName,
                         @Param("brand") String brandName,
                         @Param("minPrice") Double minPrice,
                         @Param("maxPrice") Double maxPrice,
                         Pageable pageable
    );
}
