package com.rvlt.ecommerce.model.composite;

import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;
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
