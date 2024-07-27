package com.rvlt.ecommerce.dto;

import com.rvlt.ecommerce.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Status {
  private String httpStatusCode;
  private String serverStatusCode;
  private String message;

  // OK by default
  public Status() {
    this.message = "";
    this.serverStatusCode = Constants.SERVER_STATUS_CODE.SUCCESS;
    this.httpStatusCode = String.valueOf(HttpStatus.OK.value());
  }

  @Override
  public String toString() {
    return "Status{" +
            "httpStatusCode='" + httpStatusCode + '\'' +
            ", serverStatusCode='" + serverStatusCode + '\'' +
            ", message='" + message + '\'' +
            '}';
  }
}
