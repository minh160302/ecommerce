package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheck {
  @GetMapping("")
  public ResponseMessage<String> healthcheck() {
    ResponseMessage<String> res = new ResponseMessage<>();
    res.setData("OK");
    Status status = new Status();
    status.setHttpStatusCode(HttpStatus.OK.value());
    res.setStatus(status);
    res.setStatus(status);
    return res;
  }
}
