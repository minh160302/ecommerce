package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.*;
import com.rvlt.ecommerce.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class SessionController {
  @Autowired
  private SessionService sessionService;

  @PostMapping("/add-to-cart")
  public ResponseEntity<ResponseMessage<Void>> addToCart(@RequestBody RequestMessage<HandleCartActionRq> request) {
    request.getData().setAction(Constants.CART_ACTIONS.ADD);
    ResponseMessage<Void> res = sessionService.handleCartAction(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping("/update-cart")
  public ResponseEntity<ResponseMessage<Void>> updateCart(@RequestBody RequestMessage<HandleCartActionRq> request) {
    request.getData().setAction(Constants.CART_ACTIONS.UPDATE);
    ResponseMessage<Void> res = sessionService.handleCartAction(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @DeleteMapping("")
  public ResponseEntity<ResponseMessage<Void>> deleteFromCart(@RequestBody RequestMessage<DeleteFromCartRq> request) {
    ResponseMessage<Void> res = sessionService.deleteFromCart(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @DeleteMapping("/batch")
  public ResponseEntity<ResponseMessage<Void>> deleteFromCartBatch(@RequestBody RequestMessage<DeleteFromCartBatchRq> request) {
    ResponseMessage<Void> res = sessionService.deleteFromCartBatch(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}
