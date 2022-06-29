package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.ProductModel;
import com.api.ecommerceweb.model.VariantOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductModelRepository extends JpaRepository<ProductModel, Long> {

    List<ProductModel> findByProductAndStatus(Product product, Integer status);

    Optional<ProductModel> findByIdAndProduct(Long id, Product product);

    Optional<ProductModel> findByVariantOptions(VariantOption... variantOptions);

    @Query(nativeQuery = true, value = "SELECT * FROM product_model pm JOIN product_model_variant_option p\n" +
            "ON pm.id = p.product_model_id JOIN variant_option vo ON vo.id = p.variant_option_id\n" +
            "WHERE pm.id = :modelId AND (p.variant_option_id =:optionId)")
    Optional<ProductModel> getProductModelNative(@Param("modelId") Long modelId, @Param("optionId") Long option);

    Optional<ProductModel> findByNameAndProduct(String name, Product product);

    boolean existsByIdAndVariantOptions(Long productModelId, VariantOption option);

    @Query(
            nativeQuery = true,
            value = "SELECT pm.* FROM product_model pm\n" +
                    "JOIN products p \n" +
                    "ON p.id = pm.product_id\n" +
                    "JOIN shops s \n" +
                    "ON p.shop_id = s.id\n" +
                    "WHERE p.id = :productId \n" +
                    "AND (:modelId IS NULL OR pm.id = :modelId)\n" +
                    "AND s.id = :shopId\n" +
                    "AND (max_purchase_quantity IS NULL OR :quantity <= max_purchase_quantity)\n" +
                    "AND pm.status =1 AND p.active =1"
    )
    Optional<ProductModel> getPurchaseQuantity(@Param("productId") Long productId,
                                               @Param("modelId") Long modelId,
                                               @Param("shopId") Long shopId,
                                               @Param("quantity") Integer quantity);

    List<ProductModel> findAllByVariantOptions(VariantOption variantOption);

}
