package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByName(String name);

    Optional<Inventory> findByNameIgnoreCase(String name);
}
