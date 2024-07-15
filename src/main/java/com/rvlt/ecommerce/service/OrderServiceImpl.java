package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.model.Order;
import com.rvlt.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ResponseMessage<Order> getOrderById(Long id) {
        ResponseMessage<Order> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                rs.setData(order.get());
            } else {
                status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                status.setMessage("Order not found");
            }
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }

    @Override
    public ResponseMessage<OrderStatusRs> getOrderStatus(Long id) {
        ResponseMessage<OrderStatusRs> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                Order orderObj = order.get();
                OrderStatusRs rf = new OrderStatusRs();
                rf.setStatus(orderObj.getStatus());
                rs.setData(rf);
            } else {
                status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
                status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
                status.setMessage("Order not found");
            }
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }
}
