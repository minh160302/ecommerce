package com.rvlt.ecommerce.controller;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.DeleteFromCartBatchRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartRq;
import com.rvlt.ecommerce.dto.session.HandleCartActionRq;
import com.rvlt.ecommerce.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {
  @Autowired
  private SessionService sessionService;

  @Operation(summary = "[Client] Add a product to cart")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @PostMapping("/add-to-cart")
  public ResponseEntity<ResponseMessage<Void>> addToCart(@RequestBody RequestMessage<HandleCartActionRq> request, HttpServletRequest httpServletRequest) throws Exception {
    request.getData().setAction(Constants.CART_ACTIONS.ADD);
    ResponseMessage<Void> res = sessionService.handleCartAction(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Client] Update quantity of a product in cart")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @PostMapping("/update-cart")
  public ResponseEntity<ResponseMessage<Void>> updateCart(@RequestBody RequestMessage<HandleCartActionRq> request, HttpServletRequest httpServletRequest) throws Exception {
    request.getData().setAction(Constants.CART_ACTIONS.UPDATE);
    ResponseMessage<Void> res = sessionService.handleCartAction(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Client] Delete a product from cart")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @DeleteMapping("/delete-from-cart/single")
  public ResponseEntity<ResponseMessage<Void>> deleteFromCart(@RequestBody RequestMessage<DeleteFromCartRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = sessionService.deleteFromCart(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Client] Delete multiple products from cart")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "User ID")
  @DeleteMapping("/delete-from-cart/batch")
  public ResponseEntity<ResponseMessage<Void>> deleteFromCartBatch(@RequestBody RequestMessage<DeleteFromCartBatchRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = sessionService.deleteFromCartBatch(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
