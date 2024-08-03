package com.rvlt.ecommerce.dto.wishlist;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HandleWishlistActionRq {
  private String productId;
  private String action; // "add"/"remove"
}
