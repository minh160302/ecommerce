package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.session.AddToCartRq;

public interface SessionService {
  ResponseMessage<Void> addToCart(RequestMessage<AddToCartRq> request);
}
