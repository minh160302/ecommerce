package com.rvlt.ecommerce.service;

import com.rvlt._common.model.Order;
import com.rvlt._common.model.Session;
import com.rvlt._common.model.User;
import com.rvlt._common.model.enums.OrderStatus;
import com.rvlt._common.model.enums.SessionStatus;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.user.UserOnboardingRq;
import com.rvlt.ecommerce.repository.OrderRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import com.rvlt.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private SessionRepository sessionRepository;


  @Override
  public ResponseMessage<List<User>> getAllUsers() {
    ResponseMessage<List<User>> rs = new ResponseMessage<>();
    Status status = new Status();
    List<User> users = userRepository.findAll();
    rs.setData(users);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<User> getUserById(Long id) {
    ResponseMessage<User> rs = new ResponseMessage<>();
    Status status = new Status();
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      rs.setData(user.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    rs.setStatus(status);
    return rs;
  }


  @Override
  @Transactional
  public ResponseMessage<Void> userOnboarding(RequestMessage<UserOnboardingRq> rq) {
    UserOnboardingRq input = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();

    // create user
    User user = new User();
    user.setEmail(input.getEmail());
    user.setCreatedAt(rq.getTime());
    user.setFirstName(input.getFirstName());
    user.setLastName(input.getLastName());
    user.setDob(input.getDob());

    // create new active session (ACTIVE)
    Session session = new Session();
    session.setStatus(SessionStatus.ACTIVE);
    session.setCreatedAt(rq.getTime());
    session.setUpdatedAt(rq.getTime());
    session.setTotalAmount(0.0);

    // create new order (NOT_SUBMITTED)
    Order order = new Order();
    order.setCreatedAt(rq.getTime());
    order.setHistory("");
    order.setSession(session);
    order.setStatus(OrderStatus.NOT_SUBMITTED);

    // relationship
    user.getSessions().add(session);
    session.setUser(user);
    session.setOrder(order);
    order.setSession(session);

    // commit
    userRepository.save(user);
    sessionRepository.save(session);
    orderRepository.save(order);

    status.setHttpStatusCode(HttpStatus.CREATED.value());
    rs.setStatus(status);
    return rs;
  }

}
