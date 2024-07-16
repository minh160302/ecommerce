package com.rvlt.ecommerce.dto.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateInventoryRq {
  private String name;
  private int initialCount;
//  private String description;
}
