package com.rvlt.ecommerce.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderActionRq {
  private String sessionId;
  private String userId;
}
