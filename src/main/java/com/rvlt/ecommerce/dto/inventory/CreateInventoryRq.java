package com.rvlt.ecommerce.dto.inventory;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateInventoryRq {
  @NotNull
  private String name;

  private int count;
//  private String description;

}
