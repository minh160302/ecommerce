package com.rvlt.ecommerce.service;

import com.rvlt._common.constants.Constants;
import com.rvlt._common.model.*;
import com.rvlt._common.model.enums.OrderStatus;
import com.rvlt._common.model.enums.SessionStatus;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.order.OrderActionRq;
import com.rvlt.ecommerce.dto.order.OrderStatusRs;
import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt._common.model.composite.SessionProduct;
import com.rvlt.ecommerce.rabbitmq.QueueItem;
import com.rvlt.ecommerce.rabbitmq.RabbitMQProducerService;
import com.rvlt.ecommerce.repository.*;
import com.rvlt.ecommerce.utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SessionProductRepository spRepository;

  @Autowired
  private RabbitMQProducerService mqProducerService;

  @Autowired
  private Validator validator;

  @Override
  public ResponseMessage<Order> getOrderById(Long id, HttpServletRequest httpServletRequest) {
    validator.validateUser(httpServletRequest);
    ResponseMessage<Order> rs = new ResponseMessage<>();
    Status status = new Status();
    String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
    Optional<Order> orderOpt = userIdHeader.equals(Constants.RVLT.adminHeader)
            ? orderRepository.findById(id)
            : orderRepository.findByOrderIdAndUserId(id, Long.parseLong(userIdHeader));
    if (orderOpt.isPresent()) {
      rs.setData(orderOpt.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found/Unauthorized");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<OrderStatusRs> getOrderStatus(Long id, HttpServletRequest httpServletRequest) {
    // TODO: authorize only users' orders
    validator.validateUser(httpServletRequest);
    ResponseMessage<OrderStatusRs> rs = new ResponseMessage<>();
    Status status = new Status();
    String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
    Optional<Order> orderOpt = userIdHeader.equals(Constants.RVLT.adminHeader)
            ? orderRepository.findById(id)
            : orderRepository.findByOrderIdAndUserId(id, Long.parseLong(userIdHeader));
    if (orderOpt.isPresent()) {
      Order order = orderOpt.get();
      OrderStatusRs response = new OrderStatusRs();
      response.setStatus(order.getStatus().getValue());
      rs.setData(response);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found/Unauthorized");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> submitOrder(HttpServletRequest httpServletRequest) {
    validator.validateUser(httpServletRequest);
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    String userId = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
    if (userId == null || userId.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request headers.");
    }
    SubmitOrderRq request = new SubmitOrderRq(userId);
    QueueItem item = new QueueItem();
    item.setType("submit_order");
    item.setData(request);
    mqProducerService.sendOrderMessage(item);
    /*
      @apiNote by Minh
     * - request to payment api here.
     * - Order is considered as PROCESSING if and only if payment succeeded
     * - idea:
     *    - after payment made, some available order status:
     *        - PAYMENT_PROCESSING, PAYMENT_APPROVED, PAYMENT_REJECTED
     *        - handle appropriately for each status (later)
     */
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> cancelOrder(RequestMessage<OrderActionRq> rq) {
    OrderActionRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    // check validity
    Long userId = Long.valueOf(request.getUserId());
    Long sessionId = Long.valueOf(request.getSessionId());
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId);
    }
    Optional<Session> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
    if (sessionOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found or not belong to this user.");
    }
    Session session = sessionOpt.get();
    Order order = session.getOrder();
    if (!session.getStatus().equals(SessionStatus.INACTIVE) || order.getStatus() != OrderStatus.PROCESSING_SUBMIT) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid session/order status for cancel action. Session status: " + session.getStatus() + ", Order status: " + order.getStatus());
    }
    // cancel order
    List<SessionProduct> spList = spRepository.findProductsInSession(sessionId);
    // update counts
    for (SessionProduct sp : spList) {
      Product product = sp.getProduct();
      Inventory inventory = product.getInventory();
      int returnCount = inventory.getInStockCount() + sp.getCount();
      inventory.setInStockCount(returnCount);
      inventory.setProcessingSubmitCount(inventory.getProcessingSubmitCount() - sp.getCount());
      product.setInStock(returnCount);
      int newBalance = this.calculateBalance(inventory);
      inventory.setBalance(newBalance);
      if (this.calculateBalance(inventory) != 0) {
        System.out.println("WARNING: Item [" + inventory.getName() + "]'s count does not balance: diff" + inventory.getBalance());
      }
      inventoryRepository.save(inventory);
      productRepository.save(product);
    }
    order.setStatus(OrderStatus.PROCESSING_CANCEL);
    orderRepository.save(order);
    // TODO: 1. more actions on order cancel
    // TODO: 2. When cancel order, inStock does not update count immediately, as items need time to return to inventory
    rs.setStatus(status);
    return rs;
  }

  // TODO: only call this when delivery service is ready
  @Override
  @Transactional
  public ResponseMessage<Void> initDeliverOrder(RequestMessage<OrderActionRq> rq) {
    OrderActionRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Date now = new Date();
    // check validity
    Long userId = Long.valueOf(request.getUserId());
    Long sessionId = Long.valueOf(request.getSessionId());
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId);
    }
    Optional<Session> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
    if (sessionOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found or not belong to this user.");
    }
    Session session = sessionOpt.get();
    Order order = session.getOrder();
    if (!session.getStatus().equals(SessionStatus.INACTIVE) || !order.getStatus().equals(OrderStatus.PROCESSING_SUBMIT)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid session/order status to process. Session status: " + session.getStatus() + ", Order status: " + order.getStatus());
    }
    // process order
    List<SessionProduct> spList = spRepository.findProductsInSession(sessionId);
    // update counts
    for (SessionProduct sp : spList) {
      Inventory inventory = sp.getProduct().getInventory();
      int cnt = sp.getCount();
      inventory.setProcessingSubmitCount(inventory.getProcessingSubmitCount() - cnt);
      inventory.setDeliveryInProgressCount(inventory.getDeliveryInProgressCount() + cnt);
      inventoryRepository.save(inventory);
    }
    order.setStatus(OrderStatus.DELIVERY_IN_PROGRESS);
    String currentHist = order.getHistory();
    order.setHistory(currentHist + " | " + "init_delivery_at: " + now);
    orderRepository.save(order);
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> receiveOrder(RequestMessage<OrderActionRq> rq) {
    OrderActionRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Date now = new Date();
    // check validity
    Long userId = Long.valueOf(request.getUserId());
    Long sessionId = Long.valueOf(request.getSessionId());
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId);
    }
    Optional<Session> sessionOpt = sessionRepository.findBySessionIdAndUserId(sessionId, userId);
    if (sessionOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found or not belong to this user.");
    }
    Session session = sessionOpt.get();
    Order order = session.getOrder();
    if (!session.getStatus().equals(SessionStatus.INACTIVE) || !order.getStatus().equals(OrderStatus.DELIVERY_IN_PROGRESS)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid session/order status on order receive. Session status: " + session.getStatus() + ", Order status: " + order.getStatus());
    }
    // on order receive
    List<SessionProduct> spList = spRepository.findProductsInSession(sessionId);
    // update counts
    for (SessionProduct sp : spList) {
      Inventory inventory = sp.getProduct().getInventory();
      int cnt = sp.getCount();
      inventory.setDeliveryInProgressCount(inventory.getDeliveryInProgressCount() - cnt);
      inventory.setDeliveredCount(inventory.getDeliveredCount() + cnt);
      inventoryRepository.save(inventory);
    }
    order.setStatus(OrderStatus.DELIVERED);
    String currentHist = order.getHistory();
    order.setHistory(currentHist + " | " + "order_received_at: " + now);
    orderRepository.save(order);
    rs.setStatus(status);
    return rs;
  }

  /**
   * Indirectly throws error. Don't user ResponseStatusException
   */
  @Override
  @Transactional
  public User submitOrderAction(SubmitOrderRq request) throws Exception {
    Long userId = Long.valueOf(request.getUserId());
    Optional<User> userOpt = userRepository.findById(userId);
    if (userOpt.isEmpty()) {
      throw new Exception("User not found: " + userId);
    }
    Optional<Session> optSession = sessionRepository.findActiveSessionByUser(userId);
    if (optSession.isEmpty()) {
      throw new Exception("Invalid database entity: Session");
    }
    Session session = optSession.get();
    Set<SessionProduct> spList = session.getSessionProducts();
    if (spList.isEmpty()) {
      throw new Exception("Invalid order submission: Empty cart!");
    }
    Optional<Order> orderOpt = orderRepository.findById(session.getId());
    if (orderOpt.isPresent()) {
      Date now = new Date();
      // order
      Order order = orderOpt.get();
      order.setStatus(OrderStatus.PROCESSING_SUBMIT);
      order.setSubmitted_at(now);
      order.setHistory((order.getHistory() != null ? order.getHistory() : "") + "submitted_at: " + now);
      // session
      session.setUpdatedAt(now);
      session.setStatus(SessionStatus.INACTIVE);
      // product
      for (SessionProduct sp : spList) {
        int diff = sp.getCount();
        Product product = sp.getProduct();
        Long productId = product.getId();
        Optional<Inventory> invenOpt = inventoryRepository.findById(productId);
        if (invenOpt.isPresent()) {
          Inventory inventory = invenOpt.get();
          if (inventory.getInStockCount() < diff) {
            throw new AmqpRejectAndDontRequeueException("Insufficient inventory count: id [" + inventory.getName() + "] amount: " + inventory.getInStockCount());
          }
          inventory.setInStockCount(inventory.getInStockCount() - diff);
          inventory.setInSessionHolding(inventory.getInSessionHolding() - diff);
          inventory.setProcessingSubmitCount(inventory.getProcessingSubmitCount() + diff);
          int newBalance = this.calculateBalance(inventory);
          inventory.setBalance(newBalance);
          if (this.calculateBalance(inventory) != 0) {
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
      throw new Exception("Failure in database: Order not found " + session.getId());
    }
  }

  @Override
  @Transactional
  public void afterOrderSubmitAction(User user, Date now) {
    // new ACTIVE session
    Session session = new Session();
    session.setUser(user);
    session.setCreatedAt(now);
    session.setUpdatedAt(now);
    session.setTotalAmount(0.0);
    sessionRepository.save(session);
    // create new order (NOT_SUBMITTED status)
    Order order = new Order();
    order.setStatus(OrderStatus.NOT_SUBMITTED);
    order.setCreatedAt(now);
    order.setHistory("");
    order.setSession(session);
    orderRepository.save(order);
  }

  private int calculateBalance(Inventory iv) {
    int t1 = iv.getInStockCount() + iv.getProcessingSubmitCount() + iv.getDeliveredCount() + iv.getDeliveryInProgressCount()
            + iv.getProcessingCancelCount() + iv.getCancelInProgressCount() + iv.getCancelledCount() + iv.getCancelFailedCount()
            + iv.getReturnedCount() + iv.getReturnInProgressCount() + iv.getReturnInProgressCount() + iv.getReturnFailedCount();
    return iv.getTotalCount() - t1;
  }
}
