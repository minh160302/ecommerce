package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.Wishlist;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.WishlistRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WishlistServiceImpl implements WishlistService {
  @Autowired
  private WishlistRepository wishlistRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  @Transactional
  public ResponseMessage<Void> handleWishlistAction(RequestMessage<HandleWishlistActionRq> rq, HttpServletRequest httpServletRequest) {
    HandleWishlistActionRq input = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      String userId = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
      String productId = input.getProductId();
      Optional<Product> optProduct = productRepository.findById(Long.valueOf(productId));
      if (optProduct.isEmpty()) {
        throw new Exception("Invalid request: Product not found");
      }
      Product product = optProduct.get();
      List<Wishlist> lst = wishlistRepository.findWishlistByUserId(Long.valueOf(userId));
      Wishlist wishlist = lst.get(0);
      Set<Product> products = wishlist.getProducts();
      switch (input.getAction()) {
        case Constants.WISHLIST_ACTIONS.ADD:
          products.add(product);
          break;
        case Constants.WISHLIST_ACTIONS.REMOVE:
          products.remove(product);
          break;
        default:
          throw new Exception("Invalid action");
      }
      wishlist.setProducts(products);
      wishlistRepository.save(wishlist);
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }
}
