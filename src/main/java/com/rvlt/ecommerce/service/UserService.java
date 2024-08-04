package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.user.UserOnboardingRq;
import com.rvlt.ecommerce.model.User;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface UserService {
  ResponseMessage<List<User>> getAllUsers();

  ResponseMessage<User> getUserById(Long id);

  ResponseMessage<Void> userOnboarding(RequestMessage<UserOnboardingRq> requestMessage);
}
