package com.rvlt.ecommerce.dto.session;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddToCartRq {
  private String productId;
  private String sessionId;
  private int quantity;
}
