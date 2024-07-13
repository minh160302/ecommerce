package com.rvlt.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "inventories")
public class Inventory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @Column(name = "name")
  private String name;

  @Column(name = "total_count")
  private int totalCount;

  @Column(name = "in_stock_count")
  private int inStockCount;

  @Column(name = "processing_count")
  private int processingCount;

  @Column(name = "delivered_count")
  private int deliveredCount;

  @Column(name = "in_session_holding")
  private int inSessionHolding;

  @Column(name = "balance")
  private int balance;

  @OneToOne(mappedBy = "inventory", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private Product product;
}
