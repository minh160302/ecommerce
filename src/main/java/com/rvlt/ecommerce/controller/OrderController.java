package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.order.OrderActionRq;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt.ecommerce.model.Order;
import com.rvlt.ecommerce.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
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
  public ResponseEntity<ResponseMessage<Order>> getOrderById(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
    ResponseMessage<Order> res = orderService.getOrderById(orderId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @GetMapping("/tracking/{orderId}")
  public ResponseEntity<ResponseMessage<OrderStatusRs>> getOrderStatus(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
    ResponseMessage<OrderStatusRs> res = orderService.getOrderStatus(orderId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  // TODO: refactor to userId in header
  @PostMapping("/submit")
  public ResponseEntity<ResponseMessage<Void>> submitOrder(HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = orderService.submitOrder(httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /** init order delivery complete: PROCESSING_SUBMIT -> DELIVERY_IN_PROGRESS **/
  @PostMapping("/init-delivery")
  public ResponseEntity<ResponseMessage<Void>> initDeliverOrder(@RequestBody RequestMessage<OrderActionRq> rq) {
    ResponseMessage<Void> res = orderService.initDeliverOrder(rq);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }


  /** deliver order complete: DELIVERY_IN_PROGRESS -> DELIVERED  **/
  @PostMapping("/receive")
  public ResponseEntity<ResponseMessage<Void>> receiveOrder(@RequestBody RequestMessage<OrderActionRq> rq) {
    ResponseMessage<Void> res = orderService.receiveOrder(rq);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /**
   * cancel order:
   *    - check whether users submit their own orders
   *    - whether the order is ACTIVE
   */
  @PostMapping("/cancel")
  public ResponseEntity<ResponseMessage<Void>> cancelOrder(@RequestBody RequestMessage<OrderActionRq> rq) {
    ResponseMessage<Void> res = orderService.cancelOrder(rq);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
