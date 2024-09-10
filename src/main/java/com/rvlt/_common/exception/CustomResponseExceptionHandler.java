package com.rvlt._common.exception;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessageNoData;
import com.rvlt.ecommerce.dto.Status;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
@Order(1)
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value
          = {ResponseStatusException.class, IllegalStateException.class, Exception.class})
  protected ResponseEntity<Object> handleExceptions(
          RuntimeException ex, WebRequest request) {
    ResponseMessageNoData rs = new ResponseMessageNoData();
    Status status = new Status();
    if (ex instanceof ResponseStatusException) {
      status.setHttpStatusCode(((ResponseStatusException) ex).getStatusCode().value());
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
    } else if (ex instanceof IllegalStateException) {
      status.setHttpStatusCode(HttpStatus.BAD_REQUEST.value());
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SERVER_FAILED);
    } else {
      status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SERVER_FAILED);
    }
    status.setMessage(ex.getMessage());
    rs.setStatus(status);
    return handleExceptionInternal(ex, rs, new HttpHeaders(), HttpStatus.valueOf(status.getHttpStatusCode()), request);
  }
}
