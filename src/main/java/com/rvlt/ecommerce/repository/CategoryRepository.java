package com.rvlt.ecommerce.repository;

import com.rvlt.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query(value = "select categories.* from products_categories left join categories on products_categories.category_id = categories.id where product_id = '1'",
  nativeQuery = true)
  List<Category> findCategoriesOfProduct(Long productId);
}
