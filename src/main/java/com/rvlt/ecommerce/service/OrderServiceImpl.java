package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.order.OrderRf;
import com.rvlt.ecommerce.dto.order.OrderStatusRf;
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
    public ResponseMessage<OrderRf> getOrderById(Long id) {
        ResponseMessage<OrderRf> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                Order orderObj = order.get();
                OrderRf rf = mapOrderToDTO(orderObj);
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

    @Override
    public ResponseMessage<OrderStatusRf> getOrderStatus(Long id) {
        ResponseMessage<OrderStatusRf> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                Order orderObj = order.get();
                OrderStatusRf rf = new OrderStatusRf();
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

    private OrderRf mapOrderToDTO(Order order) {
        OrderRf rf = new OrderRf();
        rf.setId(order.getId());
        rf.setStatus(order.getStatus());
        rf.setCreatedAt(order.getCreatedAt());
        rf.setSubmittedAt(order.getSubmitted_at());
        rf.setHistory(order.getHistory());
        return rf;
    }
}
