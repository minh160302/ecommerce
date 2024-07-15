package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;

import com.rvlt.ecommerce.dto.order.OrderRf;
import com.rvlt.ecommerce.dto.order.OrderStatusRf;

public interface OrderService {
    ResponseMessage<OrderRf> getOrderById(Long id);

    ResponseMessage<OrderStatusRf> getOrderStatus(Long id);
}
