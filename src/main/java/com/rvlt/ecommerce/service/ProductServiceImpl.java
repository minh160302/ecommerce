package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Category;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.Session;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import com.rvlt.ecommerce.repository.CategoryRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.SessionProductRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private SessionRepository sessionRepository;
  @Autowired
  private SessionProductRepository sessionProductRepository;
  @Autowired
  private CategoryRepository categoryRepository;
  @Autowired
  private ProductViewService productViewService;

  @Override
  public ResponseMessage<List<Product>> getAllProduct() {
    ResponseMessage<List<Product>> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      List<Product> products = productRepository.findAll();
      rs.setData(products);
    } catch (Exception e) {
      status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SERVER_FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Product> getProductById(Long id, HttpServletRequest httpServletRequest) {
    ResponseMessage<Product> rs = new ResponseMessage<>();
    Status status = new Status();

    try {
      String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
      if (userIdHeader == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request header");
      }
      Long userId = Long.parseLong(userIdHeader);
      Optional<Product> productOpt = productRepository.findById(id);
      if (productOpt.isPresent()) {
        Product product = productOpt.get();
        rs.setData(product);
        productViewService.userViewProduct(product, userId);
      } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
      }
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        status.setHttpStatusCode(((ResponseStatusException) e).getStatusCode().value());
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      } else {
        status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SERVER_FAILED);
      }
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> updateProduct(Long id, RequestMessage<UpdateProductRq> rq, HttpServletRequest httpServletRequest) {
    UpdateProductRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
      if (userIdHeader == null || !userIdHeader.equals(Constants.RVLT.adminHeader)) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized request header");
      }
      Optional<Product> productOpt = productRepository.findById(id);
      if (productOpt.isPresent()) {
        Product product = productOpt.get();

        double rqPrice = request.getPrice();
        if (rqPrice > 0) {
          double oldPrice = product.getPrice();
          double newPrice = request.getPrice();
          product.setPrice(newPrice);
          productRepository.save(product);

          // get list of sessionProduct (in ACTIVE session only) and update related sessions total_Amount
          List<SessionProduct> sessionProducts = sessionProductRepository.findActiveByProductId(product.getId());
          for (SessionProduct sp : sessionProducts) {
            Session session = sp.getSession();
            int count = sp.getCount();
            double priceDifference = (newPrice - oldPrice) * count;
            session.setTotalAmount(session.getTotalAmount() + priceDifference);
            sessionRepository.save(session);
          }
        } else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price provided");
        }
      } else {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
      }
    }
    catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        status.setHttpStatusCode(((ResponseStatusException) e).getStatusCode().value());
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      } else {
        status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SERVER_FAILED);
      }
      status.setMessage("Error updating product: " + e.getMessage());
      // Manually trigger rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<List<Category>> getProductCategories(Long productId) {
    ResponseMessage<List<Category>> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      List<Category> categories = categoryRepository.findCategoriesOfProduct(productId);
      rs.setData(categories);
    } catch (Exception e) {
      status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<List<Product>> getPersonalizedProducts(HttpServletRequest httpServletRequest) {
    ResponseMessage<List<Product>> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
      if (userIdHeader == null || userIdHeader.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request header");
      }
      Long userId = Long.parseLong(userIdHeader);
      List<Product> products = productRepository.findMostViewedProducts(userId);
      rs.setData(products);
    } catch (Exception e) {
      if (e instanceof ResponseStatusException) {
        status.setHttpStatusCode(((ResponseStatusException) e).getStatusCode().value());
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      } else {
        status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.SERVER_FAILED);
      }
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }
}
