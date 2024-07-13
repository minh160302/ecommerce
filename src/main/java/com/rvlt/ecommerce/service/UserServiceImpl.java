package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.user.UserOnboardingDto;
import com.rvlt.ecommerce.model.Order;
import com.rvlt.ecommerce.model.Session;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.repository.OrderRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import com.rvlt.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    try {
      List<User> users = userRepository.findAll();
      rs.setData(users);
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<User> getUserById(Long id) {
    ResponseMessage<User> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Optional<User> user = userRepository.findById(id);
      if (user.isPresent()) {
        rs.setData(user.get());
      } else {
        status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
        status.setMessage("User not found");
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
  public ResponseMessage<Void> userOnboarding(RequestMessage<UserOnboardingDto> rq) {
    UserOnboardingDto input = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      // create user
      User user = new User();
      user.setEmail(input.getEmail());
      user.setCreatedAt(rq.getTime());
      user.setFirstName(input.getFirstName());
      user.setLastName(input.getLastName());
      user.setDob(input.getDob());
      userRepository.save(user);
      // create new active session
      Session session = new Session();
      session.setUser(user);
      session.setStatus("ACTIVE");
      session.setCreatedAt(rq.getTime());
      session.setUpdatedAt(rq.getTime());
      sessionRepository.save(session);
      // create new order (NOT_SUBMITTED status)
      Order order = new Order();
      order.setStatus("NOT_SUBMITTED");
      order.setCreatedAt(rq.getTime());
      order.setHistory("");
      order.setSession(session);
      orderRepository.save(order);
      status.setHttpStatusCode(String.valueOf(HttpStatus.CREATED.value()));
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }
}
