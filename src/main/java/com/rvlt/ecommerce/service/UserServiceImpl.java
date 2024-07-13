package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.model.User;
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
}
