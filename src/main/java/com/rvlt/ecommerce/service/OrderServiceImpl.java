package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt.ecommerce.model.*;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import com.rvlt.ecommerce.repository.InventoryRepository;
import com.rvlt.ecommerce.repository.OrderRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private InventoryRepository inventoryRepository;

  @Override
  public ResponseMessage<Order> getOrderById(Long id) {
    ResponseMessage<Order> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Optional<Order> orderOpt = orderRepository.findById(id);
      if (orderOpt.isPresent()) {
        rs.setData(orderOpt.get());
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
      Optional<Order> orderOpt = orderRepository.findById(id);
      if (orderOpt.isPresent()) {
        Order order = orderOpt.get();
        OrderStatusRs response = new OrderStatusRs();
        response.setStatus(order.getStatus());
        rs.setData(response);
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
  @Transactional
  public ResponseMessage<Void> submitOrder(RequestMessage<SubmitOrderRq> rq) {
    SubmitOrderRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Date now = new Date();
      User currentUser = this.submitOrderAction(request);
      this.afterOrderSubmitAction(currentUser, now);
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
      // Manually trigger rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }

  @Transactional
  public User submitOrderAction(SubmitOrderRq request) throws Exception {
    Long sessionId = Long.valueOf(request.getSessionId());
    Optional<Order> orderOpt = orderRepository.findById(sessionId);
    Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
    if (orderOpt.isPresent() && sessionOpt.isPresent()) {
      Date now = new Date();
      // order
      Order order = orderOpt.get();
      order.setStatus(Constants.ORDER_STATUS.PROCESSING);
      order.setSubmitted_at(now);
      order.setHistory((order.getHistory() != null ? order.getHistory() : "") + "submited_at: " + now + "|");
      // session
      Session session = sessionOpt.get();
      session.setUpdatedAt(now);
      session.setStatus(Constants.SESSION_STATUS.INACTIVE);
      // product
      for (SessionProduct sp : session.getSessionProducts()) {
        int diff = sp.getCount();
        Product product = sp.getProduct();
        Long productId = product.getId();
        Optional<Inventory> invenOpt = inventoryRepository.findById(productId);
        if (invenOpt.isPresent()) {
          Inventory inventory = invenOpt.get();
          inventory.setInStockCount(inventory.getInStockCount() - diff);
          inventory.setInSessionHolding(inventory.getInSessionHolding() - diff);
          inventory.setProcessingCount(inventory.getProcessingCount() + diff);
          inventory.setBalance(inventory.getTotalCount() - (inventory.getInStockCount() + inventory.getProcessingCount() + inventory.getDeliveredCount()));
          if (inventory.getBalance() != 0) {
            System.out.println("WARNING: Item count does not balance: diff" + inventory.getBalance());
          }
          product.setInStock(product.getInStock() - diff);
          // commit
          inventoryRepository.save(inventory);
          productRepository.save(product);
        } else {
          throw new Exception("Failure in database: Entity not found.");
        }
      }
      sessionRepository.save(session);
      orderRepository.save(order);
      return session.getUser();
    } else {
      throw new Exception("Entity not found. Failure in database.");
    }
  }

  @Transactional
  public void afterOrderSubmitAction(User user, Date now) {
    // ACTIVE session
    Session session = new Session();
    session.setUser(user);
    session.setStatus(Constants.SESSION_STATUS.ACTIVE);
    session.setCreatedAt(now);
    session.setUpdatedAt(now);
    session.setTotalAmount(0.0);
    sessionRepository.save(session);
    // create new order (NOT_SUBMITTED status)
    Order order = new Order();
    order.setStatus(Constants.ORDER_STATUS.NOT_SUBMITTED);
    order.setCreatedAt(now);
    order.setHistory("");
    order.setSession(session);
    orderRepository.save(order);
  }
}
