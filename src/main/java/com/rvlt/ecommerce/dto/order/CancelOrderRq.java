package com.rvlt.ecommerce.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CancelOrderRq {
  private String sessionId;
  private String userId;
}
