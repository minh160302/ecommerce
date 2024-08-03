package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;

public interface WishlistService {

  ResponseMessage<Void> handleWishlistAction(String userId, RequestMessage<HandleWishlistActionRq> rq);
}
