package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.composite.ProductView;
import com.rvlt.ecommerce.model.composite.ProductViewKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView, ProductViewKey> {
}
