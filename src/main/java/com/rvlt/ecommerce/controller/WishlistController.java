package com.rvlt.ecommerce.controller;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;
import com.rvlt.ecommerce.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
  @Autowired
  private WishlistService wishlistService;

  @Operation(summary = "[Client] Add a product to wish list")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @PostMapping("/add")
  public ResponseEntity<ResponseMessage<Void>> addProductToWishlist(@RequestBody RequestMessage<HandleWishlistActionRq> rq, HttpServletRequest httpServletRequest) {
    rq.getData().setAction(Constants.WISHLIST_ACTIONS.ADD);
    ResponseMessage<Void> res = wishlistService.handleWishlistAction(rq, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Client] Remove a product from wish list")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @PostMapping("/remove")
  public ResponseEntity<ResponseMessage<Void>> removeProductFromWishlist(@RequestBody RequestMessage<HandleWishlistActionRq> rq, HttpServletRequest httpServletRequest) {
    rq.getData().setAction(Constants.WISHLIST_ACTIONS.REMOVE);
    ResponseMessage<Void> res = wishlistService.handleWishlistAction(rq, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
