package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.user.UserOnboardingRq;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("")
  public ResponseEntity<ResponseMessage<List<User>>> getUser() {
    ResponseMessage<List<User>> res = userService.getAllUsers();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<ResponseMessage<User>> getUserById(@PathVariable Long userId) {
    ResponseMessage<User> res = userService.getUserById(userId);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping("/onboarding")
  public ResponseEntity<ResponseMessage<Void>> onboardUser(@RequestBody RequestMessage<UserOnboardingRq> request) {
    ResponseMessage<Void> res = userService.userOnboarding(request);
    return new ResponseEntity<>(res, HttpStatus.CREATED);
  }
}
