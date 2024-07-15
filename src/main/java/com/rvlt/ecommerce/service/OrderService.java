package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;

import com.rvlt.ecommerce.dto.order.OrderStatusRf;
import com.rvlt.ecommerce.model.Order;

public interface OrderService {
    ResponseMessage<Order> getOrderById(Long id);

    ResponseMessage<OrderStatusRf> getOrderStatus(Long id);
}
