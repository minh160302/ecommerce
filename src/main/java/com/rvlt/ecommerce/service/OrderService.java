package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt.ecommerce.model.Order;

public interface OrderService {
  ResponseMessage<Order> getOrderById(Long id);

  ResponseMessage<OrderStatusRs> getOrderStatus(Long id);

  ResponseMessage<Void> submitOrder(RequestMessage<SubmitOrderRq> rq);
}
