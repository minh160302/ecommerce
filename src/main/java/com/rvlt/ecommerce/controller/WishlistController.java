package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;
import com.rvlt.ecommerce.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
  @Autowired
  private WishlistService wishlistService;

  @PostMapping
  public ResponseEntity<ResponseMessage<Void>> addProductToWishlist(@RequestHeader(value = "X-RVLT-userId") String userId, @RequestBody RequestMessage<HandleWishlistActionRq> rq) {
    ResponseMessage<Void> res = wishlistService.handleWishlistAction(userId, rq);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}
