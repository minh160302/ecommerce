package com.rvlt.ecommerce.dto.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateInventoryBatchRq {
  private List<CreateInventoryRq> inventories;
}
