package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.Address;
import com.api.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserAndStatus(User user, int status);

}
