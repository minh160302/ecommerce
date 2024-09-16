package com.rvlt.ecommerce.service;

import com.rvlt._common.model.Product;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WishlistService {

  ResponseMessage<Void> handleWishlistAction(RequestMessage<HandleWishlistActionRq> rq, HttpServletRequest httpServletRequest);
}
