package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.order.OrderActionRq;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt.ecommerce.model.Order;

public interface OrderService {
  ResponseMessage<Order> getOrderById(Long id);

  ResponseMessage<OrderStatusRs> getOrderStatus(Long id);

  ResponseMessage<Void> submitOrder(RequestMessage<SubmitOrderRq> rq);

  ResponseMessage<Void> cancelOrder(RequestMessage<OrderActionRq> rq);

  ResponseMessage<Void> initDeliverOrder(RequestMessage<OrderActionRq> rq);

  ResponseMessage<Void> receiveOrder(RequestMessage<OrderActionRq> rq);

  ResponseMessage<Void> testOrderError(RequestMessage<String> rq);
}
