package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.session.AddToCartRq;
import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.model.Session;
import com.rvlt.ecommerce.model.composite.SessionProduct;
import com.rvlt.ecommerce.model.composite.SessionProductKey;
import com.rvlt.ecommerce.repository.InventoryRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.repository.SessionProductRepository;
import com.rvlt.ecommerce.repository.SessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.Optional;

@Service
public class SessionServiceImpl implements SessionService {
  @Autowired
  private SessionRepository sessionRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private InventoryRepository inventoryRepository;

  @Autowired
  private SessionProductRepository spRepository;

  @Override
  @Transactional
  public ResponseMessage<Void> addToCart(RequestMessage<AddToCartRq> rq) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    AddToCartRq input = rq.getData();
    Date now = new Date();
    try {
      // get product
      Product product;
      Optional<Product> pd = productRepository.findById(Long.valueOf(input.getProductId()));
      Inventory inventory;
      Optional<Inventory> inv = inventoryRepository.findById(Long.valueOf(input.getProductId()));
      if (pd.isPresent() && inv.isPresent()) {
        product = pd.get();
        inventory = inv.get();
      } else {
        throw new Exception("Product/Inventory not found");
      }
      // get session
      Session session;
      Optional<Session> ss = sessionRepository.findById(Long.valueOf(input.getSessionId()));
      if (ss.isPresent()) {
        session = ss.get();
      } else {
        throw new Exception("Session not found");
      }
      // add product to session
      // Check existing SessionProduct
      SessionProduct sp;
      SessionProductKey spKey = new SessionProductKey();
      spKey.setProductId(product.getId());
      spKey.setSessionId(session.getId());
      Optional<SessionProduct> spOpt = spRepository.findById(spKey);
      if (spOpt.isPresent()) {
        sp = spOpt.get();
        sp.setCount(sp.getCount() + input.getQuantity());
        status.setMessage("Updated quantity in cart");
      }
      else {
        sp = new SessionProduct();
        sp.setId(spKey);
        sp.setSession(session);
        sp.setProduct(product);
        sp.setCount(input.getQuantity());
        status.setMessage("New item in cart");
      }
      product.getSessionProducts().add(sp);
      session.getSessionProducts().add(sp);
      session.setUpdatedAt(now);
      // update Inventory
      inventory.setInSessionHolding(inventory.getInSessionHolding() + input.getQuantity());
      // commit
      spRepository.save(sp);
      sessionRepository.save(session);
      productRepository.save(product);
      inventoryRepository.save(inventory);
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
      // Manually trigger rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }
}