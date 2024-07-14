package com.rvlt.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {
  @Id
  @Column(name = "id")
  private Long id;

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
}
