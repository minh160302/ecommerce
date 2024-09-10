package com.rvlt.ecommerce.controller;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.order.OrderActionRq;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt._common.model.Order;
import com.rvlt.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

  @Operation(summary = "[Admin] Get order details")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
  @Parameter(in = ParameterIn.PATH, name = "orderId", required = true, description = "Order ID")
  @GetMapping("/{orderId}")
  public ResponseEntity<ResponseMessage<Order>> getOrderById(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
    ResponseMessage<Order> res = orderService.getOrderById(orderId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Admin] Get an order's tracking status")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
  @Parameter(in = ParameterIn.PATH, name = "orderId", required = true, description = "Order ID")
  @GetMapping("/tracking/{orderId}")
  public ResponseEntity<ResponseMessage<OrderStatusRs>> getOrderStatus(@PathVariable Long orderId, HttpServletRequest httpServletRequest) {
    ResponseMessage<OrderStatusRs> res = orderService.getOrderStatus(orderId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }


  @Operation(summary = "[Admin] User submit an order")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
  @PostMapping("/submit")
  public ResponseEntity<ResponseMessage<Void>> submitOrder(HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = orderService.submitOrder(httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  /** init order delivery complete: PROCESSING_SUBMIT -> DELIVERY_IN_PROGRESS **/
  @Operation(summary = "[Admin] Initiate delivery (PROCESSING_SUBMIT -> DELIVERY_IN_PROGRESS) --refactor_needed")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
  @PostMapping("/init-delivery")
  public ResponseEntity<ResponseMessage<Void>> initDeliverOrder(@RequestBody RequestMessage<OrderActionRq> rq) {
    ResponseMessage<Void> res = orderService.initDeliverOrder(rq);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }


  /** deliver order complete: DELIVERY_IN_PROGRESS -> DELIVERED  **/
  @Operation(summary = "[Admin] Transfer package to parcel delivery vendors (DELIVERY_IN_PROGRESS -> DELIVERED) --refactor_needed")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
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
  @Operation(summary = "[Admin] Cancel an order --refactor_needed")
  @Parameter(in = ParameterIn.HEADER, name = Constants.RVLT.userIdHeader, required = true, description = "Admin/User ID")
  @PostMapping("/cancel")
  public ResponseEntity<ResponseMessage<Void>> cancelOrder(@RequestBody RequestMessage<OrderActionRq> rq) {
    ResponseMessage<Void> res = orderService.cancelOrder(rq);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
