package com.rvlt.ecommerce.utils;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class Utils {
  @Autowired
  private UserRepository userRepository;

  public void validateAdminHeader(HttpServletRequest request) {
    String userIdHeader = request.getHeader(Constants.RVLT.userIdHeader);
    if (userIdHeader == null || !userIdHeader.equals(Constants.RVLT.adminHeader)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request header");
    }
  }

  public void validateUserHeader(HttpServletRequest request) {
    String userIdHeader = request.getHeader(Constants.RVLT.userIdHeader);
    if (userIdHeader == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid request header");
    }
    if (userIdHeader.equals(Constants.RVLT.adminHeader)) {
      return;
    }
    Long userId = Long.parseLong(userIdHeader);
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
    }
  }

  public User getCurrentUser(HttpServletRequest request) {
    String userIdHeader = request.getHeader(Constants.RVLT.userIdHeader);
    if (userIdHeader == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid request header");
    }
    if (userIdHeader.equals(Constants.RVLT.adminHeader)) {
      return null;
    }
    Long userId = Long.parseLong(userIdHeader);
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
    }
    return user.get();
  }
}
