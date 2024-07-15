package com.rvlt.ecommerce.dto.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryRf {
    private Long id;
    private String name;
    private int totalCount;
    private int inStockCount;
    private int processingCount;
    private int deliveredCount;
    private int inSessionHolding;
    private int balance;
}