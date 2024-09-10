package com.rvlt._common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "total_count")
  private int totalCount;

  @Column(name = "in_stock_count")
  private int inStockCount;

  @Column(name = "processing_submit_count")
  private int processingSubmitCount;

  @Column(name = "delivery_in_progress_count")
  private int deliveryInProgressCount;

  @Column(name = "delivered_count")
  private int deliveredCount;

  @Column(name = "processing_cancel_count")
  private int processingCancelCount;

  @Column(name = "cancelled_count")
  private int cancelledCount;

  @Column(name = "cancel_in_progress_count")
  private int cancelInProgressCount;

  @Column(name = "returned_count")
  private int returnedCount;

  @Column(name = "return_in_progress_count")
  private int returnInProgressCount;

  @Column(name = "delivery_failed")
  private int deliveryFailedCount;

  @Column(name = "return_failed")
  private int returnFailedCount;

  @Column(name = "cancel_failed")
  private int cancelFailedCount;

  @Column(name = "in_session_holding")
  private int inSessionHolding;

  @Column(name = "balance")
  private int balance;

  @OneToOne(mappedBy = "inventory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  @JsonIgnore
  private Product product;

  public void initializeInventory(int initialCount) {
    this.totalCount = initialCount;
    this.inStockCount = initialCount;
    this.processingSubmitCount = 0;
    this.deliveredCount = 0;
    this.inSessionHolding = 0;
    this.balance = 0;
    this.processingCancelCount = 0;
    this.cancelledCount = 0;
    this.cancelInProgressCount = 0;
    this.returnedCount = 0;
    this.returnInProgressCount = 0;
    this.deliveryFailedCount = 0;
    this.returnFailedCount = 0;
    this.cancelFailedCount = 0;
  }
}
