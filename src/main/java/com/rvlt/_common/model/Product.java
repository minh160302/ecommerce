package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt._common.model.composite.ProductCategory;
import com.rvlt._common.model.composite.SessionProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "in_stock")
  private int inStock;

  @Column(name = "price")
  private double price;

  // whoever owns the foreign key column gets the @JoinColumn annotation.
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  @JsonIgnore
  private Inventory inventory;

  @JsonIgnore
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  Set<SessionProduct> sessionProducts;

  @JsonIgnore
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  Set<ProductCategory> productCategories;

  public Product() {
    this.sessionProducts = new HashSet<>();
    this.productCategories = new HashSet<>();
  }
}
