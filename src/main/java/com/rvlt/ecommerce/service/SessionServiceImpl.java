package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.session.AddToCartRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartBatchRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartRq;
import com.rvlt.ecommerce.dto.session.UpdateCountRq;
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
      } else {
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

  @Override
  @Transactional
  public ResponseMessage<Void> updateProductCount(RequestMessage<UpdateCountRq> request) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    UpdateCountRq input = request.getData();
    Date now = new Date();
    try {
      // Get Session Product
      SessionProduct sp;
      SessionProductKey spKey = new SessionProductKey();
      spKey.setProductId(input.getProductId());
      spKey.setSessionId(input.getSessionId());
      Optional<SessionProduct> spOpt = spRepository.findById(spKey);
      if (spOpt.isPresent()) {
        sp = spOpt.get();
      } else {
        throw new Exception("Invalid request or IDs not found.");
      }
      // calculate count diff to update
      int oldCount = sp.getCount();
      int countDifference = input.getCount() - oldCount;
      // Get Inventory
      Inventory inventory;
      Optional<Inventory> invOpt = inventoryRepository.findById(input.getProductId());
      if (invOpt.isPresent()) {
        inventory = invOpt.get();
        if (inventory.getInStockCount() < input.getCount() || input.getCount() >= 1000) {
          throw new Exception("Too many items ordered.");
        }
        // update in_session_holding + count diff(can be negative but res > 0)
        inventory.setInSessionHolding(inventory.getInSessionHolding() + countDifference);
      } else {
        throw new Exception("Inventory not found.");
      }
      // update count in session_products
      sp.setCount(input.getCount());
      // Get Session, Product to update total_amount and get price
      Session session = sp.getSession();
      Product product = sp.getProduct();
      double priceDifference = product.getPrice() * countDifference;
      session.setTotalAmount(session.getTotalAmount() + priceDifference);
      // update time
      session.setUpdatedAt(now);

      // commit
      spRepository.save(sp);
      inventoryRepository.save(inventory);
      sessionRepository.save(session);
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

  @Override
  @Transactional
  public ResponseMessage<Void> deleteFromCart(RequestMessage<DeleteFromCartRq> request) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    DeleteFromCartRq input = request.getData();
    Date now = new Date();
    try {
      // Get Session Product
      SessionProduct sp;
      SessionProductKey spKey = new SessionProductKey();
      spKey.setProductId(input.getProductId());
      spKey.setSessionId(input.getSessionId());
      Optional<SessionProduct> spOpt = spRepository.findById(spKey);
      if (spOpt.isPresent()) {
        sp = spOpt.get();
        int countToRemove = sp.getCount();
        if (countToRemove <= 0) {
          throw new IllegalStateException("Invalid count to remove");
        }
        Session session = sp.getSession();
        Product product = sp.getProduct();
        // update in_session_holding in Inventory
        Inventory inventory = product.getInventory();
        if (inventory != null) {
          inventory.setInSessionHolding(inventory.getInSessionHolding() - countToRemove);
          inventoryRepository.save(inventory);
        }
        // update total_amount in session
        // calc price diff to subtract total amount, should there be checks ?
        double priceDifference = product.getPrice() * countToRemove;
        double newTotalAmount = session.getTotalAmount() - priceDifference;
        if (newTotalAmount < 0) {
          throw new IllegalStateException("Session total amount would become negative");
        }
        session.setTotalAmount(newTotalAmount);
        session.setUpdatedAt(now);
        // java consistency, delete SP object in java mem (redundant ?)
        // from research, if we continue using the same Session object, the sp will still be there without this line (in theory).
        session.getSessionProducts().remove(sp);
        // commit updated session
        sessionRepository.save(session);

        // commit delete sp from sp table
        spRepository.delete(sp);
      } else {
        throw new Exception("Invalid request or IDs not found.");
      }
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

  @Override
  @Transactional
  public ResponseMessage<Void> deleteFromCartBatch(RequestMessage<DeleteFromCartBatchRq> request) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    DeleteFromCartBatchRq input = request.getData();
    Date now = new Date();

    try {
      if (input.getProducts() == null || input.getProducts().isEmpty()) {
        throw new IllegalStateException("Invalid request or products not found.");
      }
      Session session;
      Long sessionId = input.getSessionId();
      Optional<Session> sessionOpt = sessionRepository.findById(sessionId);
      if (sessionOpt.isPresent()) {
        session = sessionOpt.get();
      } else {
        throw new Exception("Session not found.");
      }
      // helper var for total_amount
      double totalPriceDiff = 0;

      for (DeleteFromCartRq product : input.getProducts()) {
        SessionProductKey spKey = new SessionProductKey();
        spKey.setProductId(product.getProductId());
        spKey.setSessionId(sessionId);
        Optional<SessionProduct> spOpt = spRepository.findById(spKey);

        if (spOpt.isPresent()) {
          SessionProduct sp = spOpt.get();
          int countToRemove = sp.getCount();
          if (countToRemove <= 0) {
            throw new IllegalStateException("Invalid count to remove");
          }

          // update in_session_holding
          Product productObj = sp.getProduct();
          Inventory inventory = productObj.getInventory();
          if (inventory != null) {
            inventory.setInSessionHolding(inventory.getInSessionHolding() - countToRemove);
            inventoryRepository.save(inventory);
          }

          // get each price diff and increment
          double priceDifference = productObj.getPrice() * countToRemove;
          totalPriceDiff += priceDifference;
          // delete sp object
          session.getSessionProducts().remove(sp);
          // commit delete sp
          spRepository.delete(sp);
        } else {
          // this stops the batch though, one item might be wrong, but it should continue
          throw new Exception("Product " + product.getProductId() + " not found in session " + sessionId);
        }
      }
      // update session total_amount
      double newTotalAmount = session.getTotalAmount() - totalPriceDiff;
      if (newTotalAmount < 0) {
        throw new IllegalStateException("Session total amount would become negative");
      }
      session.setTotalAmount(newTotalAmount);
      session.setUpdatedAt(now);
      // commit update session
      sessionRepository.save(session);

    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
      // Manual rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }
}
