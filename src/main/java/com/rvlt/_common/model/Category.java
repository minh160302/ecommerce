package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rvlt._common.model.composite.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.annotation.Order;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "name", unique = true)
  private String name;

  @NotNull
  @Column(name = "description")
  private String description;

  @Column(name = "active")
  private boolean active;

  @JsonIgnore
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  Set<ProductCategory> productCategories;
}
