package com.rvlt._common.model.composite;

import com.rvlt._common.model.Product;
import com.rvlt._common.model.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products_categories")
public class ProductCategory {
  @EmbeddedId
  private ProductCategoryKey id;

  @ManyToOne
  @MapsId("productId")
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @MapsId("categoryId")
  @JoinColumn(name = "category_id")
  private Category category;

  @Override
  public String toString() {
    return "ProductCategory{" +
            "id=" + id +
            ", product=" + product +
            ", category=" + category +
            '}';
  }
}
