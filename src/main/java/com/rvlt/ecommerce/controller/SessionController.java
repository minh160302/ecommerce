package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.AddToCartRq;
import com.rvlt.ecommerce.dto.session.UpdateCountRq;
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
  public ResponseEntity<ResponseMessage<Void>> addToCart(@RequestBody RequestMessage<AddToCartRq> request) {
    ResponseMessage<Void> res = sessionService.addToCart(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping("/update-cart")
  public ResponseEntity<ResponseMessage<Void>> updateProductCount(@RequestBody RequestMessage<UpdateCountRq> request) {
    ResponseMessage<Void> res = sessionService.updateProductCount(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}
