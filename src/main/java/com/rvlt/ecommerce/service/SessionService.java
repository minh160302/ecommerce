package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.DeleteFromCartBatchRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartRq;
import com.rvlt.ecommerce.dto.session.HandleCartActionRq;
import jakarta.servlet.http.HttpServletRequest;

public interface SessionService {
  ResponseMessage<Void> deleteFromCart(RequestMessage<DeleteFromCartRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> deleteFromCartBatch(RequestMessage<DeleteFromCartBatchRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> handleCartAction(RequestMessage<HandleCartActionRq> request, HttpServletRequest httpServletRequest) throws Exception;
}
