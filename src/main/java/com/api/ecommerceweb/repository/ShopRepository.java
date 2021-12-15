package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Shop;
import com.api.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {

    Optional<Shop> findByOwners(User user);
}
