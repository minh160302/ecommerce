package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query(value = "select products.* from products_categories left join products on products_categories.product_id = products.id where category_id = ?1", nativeQuery = true)
  List<Product> findProductsInCategory(Long categoryId);
}
