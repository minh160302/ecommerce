package com.rvlt.ecommerce.model.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class ProductViewKey implements Serializable {
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "product_id")
  private Long productId;

  public ProductViewKey() {

  }

  public ProductViewKey(Long userId, Long productId) {
    this.userId = userId;
    this.productId = productId;
  }
}
