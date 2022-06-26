package com.api.ecommerceweb.service;

import com.api.ecommerceweb.model.Product;
import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.repository.OrderItemRepository;
import com.api.ecommerceweb.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ProductService")
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final OrderItemRepository orderItemRepo;

    public List<Product> getNewestProducts() {
        List<Product> newestProducts =
                productRepo.findAllByOrderByCreateDate();
        return newestProducts;
    }

    public List<Product> search(String name, String category, String brand, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepo.search(name, category, brand, minPrice, maxPrice, pageable);
    }

    public Product getProduct(Long id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        return null;
    }

    public int countSolidNumber(Product p) {
        return orderItemRepo.countOrderItemByProduct(p);
    }


    public Product saveProduct(Product product) {
        return productRepo.saveAndFlush(product);
    }

    public boolean existInShop(Shop shop){
        return productRepo.existsByShop(shop);
    }

    public Product getById(Long id){
        return productRepo.findById(id).orElse(null);
    }
    
    public List<Product> getProductsInShop(Long shopId,Integer status, Pageable pageable){
        return productRepo.getProductsInShop(shopId,status,pageable);
    }
}
