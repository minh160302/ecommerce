package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.DeleteFromCartBatchRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartRq;
import com.rvlt.ecommerce.dto.session.HandleCartActionRq;
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
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("/update-cart")
  public ResponseEntity<ResponseMessage<Void>> updateCart(@RequestBody RequestMessage<HandleCartActionRq> request) {
    request.getData().setAction(Constants.CART_ACTIONS.UPDATE);
    ResponseMessage<Void> res = sessionService.handleCartAction(request);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @DeleteMapping("/delete-from-cart/single")
  public ResponseEntity<ResponseMessage<Void>> deleteFromCart(@RequestBody RequestMessage<DeleteFromCartRq> request) {
    ResponseMessage<Void> res = sessionService.deleteFromCart(request);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @DeleteMapping("/delete-from-cart/batch")
  public ResponseEntity<ResponseMessage<Void>> deleteFromCartBatch(@RequestBody RequestMessage<DeleteFromCartBatchRq> request) {
    ResponseMessage<Void> res = sessionService.deleteFromCartBatch(request);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
