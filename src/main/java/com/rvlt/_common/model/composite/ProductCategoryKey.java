package com.rvlt._common.model.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ProductCategoryKey implements Serializable {
  @Column(name = "product_id")
  private Long productId;

  @Column(name = "category_id")
  private Long categoryId;

  public ProductCategoryKey(Long productId, Long categoryId) {
    this.productId = productId;
    this.categoryId = categoryId;
  }

  public ProductCategoryKey() {
  }
}
