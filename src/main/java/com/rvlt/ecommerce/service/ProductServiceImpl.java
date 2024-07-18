package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.product.UpdateProductRq;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.Session;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.SessionProductRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

  @Override
  public ResponseMessage<List<Product>> getAllProduct() {
    ResponseMessage<List<Product>> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      List<Product> products = productRepository.findAll();
      rs.setData(products);
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Product> getProductById(Long id) {
    ResponseMessage<Product> rs = new ResponseMessage<>();
    Status status = new Status();

    try {
      Optional<Product> productOpt = productRepository.findById(id);
      if (productOpt.isPresent()) {
        rs.setData(productOpt.get());
      } else {
        status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
        status.setMessage("Product not found");
      }
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> updateProduct(Long id, RequestMessage<UpdateProductRq> rq) {
    UpdateProductRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Optional<Product> productOpt = productRepository.findById(id);
      if (productOpt.isPresent()) {
        Product product = productOpt.get();

        if (request.getPrice() > 0) {
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
          status.setHttpStatusCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
          status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
          status.setMessage("Invalid Price provided");
        }
      } else {
        status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
        status.setMessage("Product not found");
      }
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage("Error updating product: " + e.getMessage());
      // Manually trigger rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }
}
