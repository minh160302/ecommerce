package com.rvlt.ecommerce.model;

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

  // whoever owns the foreign key column gets the @JoinColumn annotation.
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  private Inventory inventory;

  @OneToMany(mappedBy = "product")
  Set<SessionProduct> sessionProducts;
}
