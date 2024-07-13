package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.user.UserOnboardingDto;
import com.rvlt.ecommerce.model.User;

import java.util.List;

public interface UserService {
  ResponseMessage<List<User>> getAllUsers();

  ResponseMessage<User> getUserById(Long id);

  ResponseMessage<Void> userOnboarding(RequestMessage<UserOnboardingDto> requestMessage);
}
