package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.FileUpload;
import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.ProductImage;
import com.api.ecommerceweb.model.ProductImgId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, ProductImgId> {


    List<ProductImage> findByProductAndStatus(Product product, Integer i);

    Optional<ProductImage> findByProductAndImageAndStatus(Product product, FileUpload image,Integer status);
}
