package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.order.OrderActionRq;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt._common.model.Order;
import com.rvlt._common.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface OrderService {
  ResponseMessage<Order> getOrderById(Long id, HttpServletRequest httpServletRequest);

  ResponseMessage<OrderStatusRs> getOrderStatus(Long id, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> submitOrder(HttpServletRequest httpServletRequest);

  ResponseMessage<Void> cancelOrder(RequestMessage<OrderActionRq> rq);

  ResponseMessage<Void> initDeliverOrder(RequestMessage<OrderActionRq> rq);

  ResponseMessage<Void> receiveOrder(RequestMessage<OrderActionRq> rq);

  @Transactional
  User submitOrderAction(SubmitOrderRq request) throws Exception;

  @Transactional
  void afterOrderSubmitAction(User user, Date now);
}
