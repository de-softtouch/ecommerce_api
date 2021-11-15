package com.api.ecommerceweb.repository;

import com.api.ecommerceweb.model.ShipmentUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentUnitRepository extends JpaRepository<ShipmentUnit,Long> {
}
