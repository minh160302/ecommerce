package com.rvlt.ecommerce.repository;

import com.rvlt._common.model.composite.ProductCategory;
import com.rvlt._common.model.composite.ProductCategoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategoryKey> {
}
