package com.rvlt.ecommerce.utils;

import com.rvlt._common.constants.Constants;
import com.rvlt._common.model.User;
import com.rvlt._common.model.enums.Role;
import com.rvlt.ecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

// TODO: differentiate UNAUTHORIZED / FORBIDDEN cases
@Component
public class Validator {
  @Autowired
  private UserRepository userRepository;

  public void validateAdmin(HttpServletRequest request) {
    User user = this.getCurrentUser(request);
    if (user != null) {
      Role role = user.getRole();
      if (role == Role.rvlt_admin || role == Role.rvlt_mod) {
        return;
      }
    }
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request");
  }

  public void validateUser(HttpServletRequest request) {
    this.getCurrentUser(request);
  }

  public User getCurrentUser(HttpServletRequest request) {
    String userIdHeader = request.getHeader(Constants.RVLT.userIdHeader);
    if (userIdHeader == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid request header");
    }
    Long userId = Long.parseLong(userIdHeader);
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
    }
    return user.get();
  }
}
