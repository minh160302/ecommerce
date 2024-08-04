package com.rvlt.ecommerce.dto.wishlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HandleWishlistActionRq {
  private String productId;
  @JsonIgnore
  private String action; // "add"/"remove"
}
