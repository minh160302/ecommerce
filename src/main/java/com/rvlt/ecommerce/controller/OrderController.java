package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt.ecommerce.model.Order;
import com.rvlt.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
  @Autowired
  private OrderService orderService;

  @GetMapping("/{orderId}")
  public ResponseEntity<ResponseMessage<Order>> getOrderById(@PathVariable Long orderId) {
    ResponseMessage<Order> res = orderService.getOrderById(orderId);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/tracking/{orderId}")
  public ResponseEntity<ResponseMessage<OrderStatusRs>> getOrderStatus(@PathVariable Long orderId) {
    ResponseMessage<OrderStatusRs> res = orderService.getOrderStatus(orderId);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping("/submit")
  public ResponseEntity<ResponseMessage<Void>> submitOrder(@RequestBody SubmitOrderRq request) {
    ResponseMessage<Void> res = orderService.submitOrder(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}
