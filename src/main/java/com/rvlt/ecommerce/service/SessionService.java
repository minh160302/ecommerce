package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.dto.session.AddToCartRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartRq;
import com.rvlt.ecommerce.dto.session.UpdateCountRq;

public interface SessionService {
  ResponseMessage<Void> addToCart(RequestMessage<AddToCartRq> request);

  ResponseMessage<Void> updateProductCount(RequestMessage<UpdateCountRq> request);

  ResponseMessage<Void> deleteFromCart(RequestMessage<DeleteFromCartRq> request);
}
