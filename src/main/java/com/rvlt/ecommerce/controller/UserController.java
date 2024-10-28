package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.user.UserOnboardingRq;
import com.rvlt._common.model.User;
import com.rvlt.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

  @Operation(summary = "[Admin] Get system users")
  @GetMapping("")
  public ResponseEntity<ResponseMessage<List<User>>> getUser() {
    ResponseMessage<List<User>> res = userService.getAllUsers();
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Admin] Get user details")
  @Parameter(in = ParameterIn.PATH, name = "userId", required = true, description = "User ID")
  @GetMapping("/{userId}")
  public ResponseEntity<ResponseMessage<User>> getUserById(@PathVariable Long userId) {
    ResponseMessage<User> res = userService.getUserById(userId);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @Operation(summary = "[Client] Onboarding new user")
  @PostMapping("/onboarding")
  public ResponseEntity<ResponseMessage<Void>> onboardUser(@RequestBody RequestMessage<UserOnboardingRq> request) {
    ResponseMessage<Void> res = userService.userOnboarding(request);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
