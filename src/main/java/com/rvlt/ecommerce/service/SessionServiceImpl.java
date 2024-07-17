package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.session.*;
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
  public ResponseMessage<Void> deleteFromCart(RequestMessage<DeleteFromCartRq> request) {
    return null;
  }

  @Override
  public ResponseMessage<Void> deleteFromCartBatch(RequestMessage<DeleteFromCartBatchRq> request) {
    return null;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> handleCartAction(RequestMessage<HandleCartActionRq> request) {
    HandleCartActionRq input = request.getData();
    String action = input.getAction();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Date now = new Date();
    try {
      // get product
      Product product;
      Optional<Product> pd = productRepository.findById(Long.valueOf(input.getProductId()));
      Inventory inventory;
      Optional<Inventory> inv = inventoryRepository.findById(Long.valueOf(input.getProductId()));
      if (pd.isEmpty() || inv.isEmpty()) {
        throw new Exception("Product/Inventory not found");
      }
      product = pd.get();
      inventory = inv.get();
      // get session
      Session session;
      Optional<Session> ss = sessionRepository.findById(Long.valueOf(input.getSessionId()));
      if (ss.isEmpty()) {
        throw new Exception("Session not found");
      }
      session = ss.get();
      // add product to session
      SessionProduct sp;
      SessionProductKey spKey = new SessionProductKey(session.getId(), product.getId());
      Optional<SessionProduct> spOpt = spRepository.findById(spKey);
      // UPDATE when NO product in cart
      if (spOpt.isEmpty() && action.equals(Constants.CART_ACTIONS.UPDATE)) {
        throw new Exception("Product not found in cart");
      }
      // ADD when NO product in cart
      else if (spOpt.isEmpty() && action.equals(Constants.CART_ACTIONS.ADD)) {
        sp = new SessionProduct();
        addToCart(sp, spKey, session, product, inventory, input.getQuantity(), now);
        status.setMessage("New item in cart");
      }
      // UPDATE when product in cart
      else if (spOpt.isPresent() && action.equals(Constants.CART_ACTIONS.UPDATE)) {
        System.out.println(input.getQuantity());
        sp = spOpt.get();
        updateCart(sp, session, inventory, input.getQuantity(), now);
        status.setMessage("Updated quantity in cart");
      }
      // ADD when product in cart
      else if (spOpt.isPresent() && action.equals(Constants.CART_ACTIONS.ADD)) {
        sp = spOpt.get();
        updateCart(sp, session, inventory, sp.getCount() + input.getQuantity(), now);
        status.setMessage("Updated quantity in cart");
      }
      else {
        throw new Exception("Invalid cart action");
      }
      // commit
      spRepository.save(sp);
      sessionRepository.save(session);
      productRepository.save(product);
      inventoryRepository.save(inventory);
    }
    catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
      // Manually trigger rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }

  @Transactional
  public void addToCart(SessionProduct sp, SessionProductKey spKey, Session session, Product product, Inventory inventory, int quantity, Date now) {
    sp.setId(spKey);
    sp.setSession(session);
    sp.setProduct(product);
    sp.setCount(quantity);

    product.getSessionProducts().add(sp);
    session.getSessionProducts().add(sp);
    session.setUpdatedAt(now);
    // update Inventory
    inventory.setInSessionHolding(inventory.getInSessionHolding() + quantity);
  }

  @Transactional
  public void updateCart(SessionProduct sp, Session session, Inventory inventory, int quantity, Date now) {
    int diff = quantity - sp.getCount();
    sp.setCount(quantity);
    inventory.setInSessionHolding(inventory.getInSessionHolding() + diff);
    session.setUpdatedAt(now);
  }
}
