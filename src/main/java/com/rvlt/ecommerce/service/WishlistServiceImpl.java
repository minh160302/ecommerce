package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.User;
import com.rvlt.ecommerce.model.Wishlist;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.WishlistRepository;
import com.rvlt.ecommerce.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {
  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private Utils utils;

  @Override
  @Transactional
  public ResponseMessage<Void> handleWishlistAction(RequestMessage<HandleWishlistActionRq> rq, HttpServletRequest httpServletRequest) {
    User user = utils.getCurrentUser(httpServletRequest);
    HandleWishlistActionRq input = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    String userId = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
    String productId = input.getProductId();
    Optional<Product> optProduct = productRepository.findById(Long.parseLong(productId));
    if (optProduct.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request: Product not found");
    }
    Product product = optProduct.get();
    Optional<Wishlist> optWishlist = wishlistRepository.findWishlistByUserId(Long.parseLong(userId));
    Wishlist wishlist;
    Set<Product> products;
    if (optWishlist.isEmpty()) {
      wishlist = new Wishlist();
      wishlist.setUser(user);
      products = new HashSet<>();
    } else {
      wishlist = optWishlist.get();
      products = wishlist.getProducts();
    }
    switch (input.getAction()) {
      case Constants.WISHLIST_ACTIONS.ADD:
        products.add(product);
        break;
      case Constants.WISHLIST_ACTIONS.REMOVE:
        products.remove(product);
        break;
      default:
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid action");
    }
    wishlist.setProducts(products);
    wishlistRepository.save(wishlist);
    rs.setStatus(status);
    return rs;
  }
}
