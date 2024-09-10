package com.rvlt.ecommerce.dto.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HandleCartActionRq {
  private String productId;
//  private String sessionId;
  private Integer quantity;
  @JsonIgnore
  private String action;
}
