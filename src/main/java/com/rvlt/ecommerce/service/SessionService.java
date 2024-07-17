package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.*;

public interface SessionService {
  ResponseMessage<Void> deleteFromCart(RequestMessage<DeleteFromCartRq> request);

  ResponseMessage<Void> deleteFromCartBatch(RequestMessage<DeleteFromCartBatchRq> request);

  ResponseMessage<Void> handleCartAction(RequestMessage<HandleCartActionRq> request);
}
