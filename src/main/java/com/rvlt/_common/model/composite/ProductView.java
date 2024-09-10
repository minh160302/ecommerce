package com.rvlt._common.model.composite;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "product_view")
@NoArgsConstructor
public class ProductView {
  @EmbeddedId
  private ProductViewKey id;

  @Column(name = "count")
  private Long count;

  @Column(name = "history")
  private String history;

  public ProductView(ProductViewKey id) {
    this.id = id;
    this.count = 0L;
    this.history = "";
  }
}
