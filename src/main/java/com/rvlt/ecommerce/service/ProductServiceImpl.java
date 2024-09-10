package com.rvlt.ecommerce.service;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt._common.model.Category;
import com.rvlt._common.model.Product;
import com.rvlt._common.model.Session;
import com.rvlt._common.model.composite.SessionProduct;
import com.rvlt.ecommerce.repository.CategoryRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.SessionProductRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import com.rvlt.ecommerce.utils.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  @Autowired
  private Validator validator;

  @Override
  public ResponseMessage<List<Product>> getAllProduct(HttpServletRequest httpServletRequest) {
    validator.validateUser(httpServletRequest);
    ResponseMessage<List<Product>> rs = new ResponseMessage<>();
    Status status = new Status();
    List<Product> products = productRepository.findAll();
    rs.setData(products);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Product> getProductById(Long id, HttpServletRequest httpServletRequest) {
    validator.validateUser(httpServletRequest);
    ResponseMessage<Product> rs = new ResponseMessage<>();
    Status status = new Status();
    String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
    Optional<Product> productOpt = productRepository.findById(id);
    if (productOpt.isPresent()) {
      Product product = productOpt.get();
      rs.setData(product);
      if (!userIdHeader.equals(Constants.RVLT.adminHeader)) {
        Long userId = Long.parseLong(userIdHeader);
        productViewService.userViewProduct(product, userId);
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> updateProduct(Long id, RequestMessage<UpdateProductRq> rq, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    UpdateProductRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
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
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error updating product: Invalid price provided");
      }
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error updating product: Product not found");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<List<Category>> getProductCategories(Long productId, HttpServletRequest httpServletRequest) {
    validator.validateUser(httpServletRequest);
    ResponseMessage<List<Category>> rs = new ResponseMessage<>();
    Status status = new Status();
    List<Category> categories = categoryRepository.findCategoriesOfProduct(productId);
    rs.setData(categories);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<List<Product>> getPersonalizedProducts(HttpServletRequest httpServletRequest) {
    validator.validateUser(httpServletRequest);
    ResponseMessage<List<Product>> rs = new ResponseMessage<>();
    Status status = new Status();
    String userIdHeader = httpServletRequest.getHeader(Constants.RVLT.userIdHeader);
    if (userIdHeader.equals(Constants.RVLT.adminHeader)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No personalized data on this resource for admin");
    }
    else {
      Long userId = Long.parseLong(userIdHeader);
      List<Product> products = productRepository.findMostViewedProducts(userId);
      rs.setData(products);
    }
    rs.setStatus(status);
    return rs;
  }
}
