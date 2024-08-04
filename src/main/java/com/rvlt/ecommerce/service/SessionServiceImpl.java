package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.session.DeleteFromCartBatchRq;
import com.rvlt.ecommerce.dto.session.DeleteFromCartRq;
import com.rvlt.ecommerce.dto.session.HandleCartActionRq;
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
import org.springframework.web.server.ResponseStatusException;

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
  public ResponseMessage<Void> deleteFromCart(RequestMessage<DeleteFromCartRq> request) {
    DeleteFromCartRq input = request.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Date now = new Date();
    // get product
    Product product;
    Optional<Product> pd = productRepository.findById(Long.valueOf(input.getProductId()));
    Inventory inventory;
    Optional<Inventory> inv = inventoryRepository.findById(Long.valueOf(input.getProductId()));
    if (pd.isEmpty() || inv.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product/Inventory not found");
    }
    product = pd.get();
    inventory = inv.get();
    // get session
    Session session;
    Optional<Session> ss = sessionRepository.findById(Long.valueOf(input.getSessionId()));
    if (ss.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
    }
    session = ss.get();
    deleteSingleProductFromCart(session, product, inventory);
    session.setUpdatedAt(now);

    sessionRepository.save(session);
    inventoryRepository.save(inventory);

    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> deleteFromCartBatch(RequestMessage<DeleteFromCartBatchRq> request) {
    DeleteFromCartBatchRq input = request.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Date now = new Date();
    // get session
    Session session;
    Optional<Session> ss = sessionRepository.findById(Long.valueOf(input.getSessionId()));
    if (ss.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
    }
    session = ss.get();
    for (String productId : input.getProducts()) {
      // get product
      Product product;
      Optional<Product> pd = productRepository.findById(Long.valueOf(productId));
      Inventory inventory;
      Optional<Inventory> inv = inventoryRepository.findById(Long.valueOf(productId));
      if (pd.isEmpty() || inv.isEmpty()) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product/Inventory not found");
      }
      product = pd.get();
      inventory = inv.get();
      deleteSingleProductFromCart(session, product, inventory);
      session.setUpdatedAt(now);
      sessionRepository.save(session);
      inventoryRepository.save(inventory);
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> handleCartAction(RequestMessage<HandleCartActionRq> request) throws Exception {
    HandleCartActionRq input = request.getData();
    String action = input.getAction();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Date now = new Date();
    // get product
    Product product;
    Optional<Product> pd = productRepository.findById(Long.valueOf(input.getProductId()));
    Inventory inventory;
    Optional<Inventory> inv = inventoryRepository.findById(Long.valueOf(input.getProductId()));
    if (pd.isEmpty() || inv.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product/Inventory not found");
    }
    product = pd.get();
    inventory = inv.get();
    // get session
    Session session;
    Optional<Session> ss = sessionRepository.findById(Long.valueOf(input.getSessionId()));
    if (ss.isEmpty())
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found");
    session = ss.get();
    if (session.getStatus().equals(Constants.SESSION_STATUS.INACTIVE))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session inactive");
    // session-product
    SessionProduct sp;
    SessionProductKey spKey = new SessionProductKey(session.getId(), product.getId());
    Optional<SessionProduct> spOpt = spRepository.findById(spKey);
    // UPDATE when NO product in cart
    if (spOpt.isEmpty() && action.equals(Constants.CART_ACTIONS.UPDATE)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found in cart");
    }
    // ADD when NO product in cart
    else if (spOpt.isEmpty() && action.equals(Constants.CART_ACTIONS.ADD)) {
      sp = new SessionProduct();
      addToCart(sp, spKey, session, product, inventory, input.getQuantity(), now);
      status.setMessage("New item in cart");
    }
    // UPDATE when product in cart
    else if (spOpt.isPresent() && action.equals(Constants.CART_ACTIONS.UPDATE)) {
      sp = spOpt.get();
      updateCart(sp, session, product, inventory, input.getQuantity(), now);
      status.setMessage("Updated quantity in cart");
    }
    // ADD when product in cart
    else if (spOpt.isPresent() && action.equals(Constants.CART_ACTIONS.ADD)) {
      sp = spOpt.get();
      updateCart(sp, session, product, inventory, sp.getCount() + input.getQuantity(), now);
      status.setMessage("Updated quantity in cart");
    } else {
      throw new Exception("Invalid cart action");
    }
    // commit
    spRepository.save(sp);
    sessionRepository.save(session);
    productRepository.save(product);
    inventoryRepository.save(inventory);

    rs.setStatus(status);
    return rs;
  }

  public void addToCart(SessionProduct sp, SessionProductKey spKey, Session session, Product product, Inventory inventory, int quantity, Date now) throws ResponseStatusException {
    if (quantity > inventory.getInStockCount()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "In stock limit exceeded!");
    }
    sp.setId(spKey);
    sp.setSession(session);
    sp.setProduct(product);
    sp.setCount(quantity);

    product.getSessionProducts().add(sp);
    session.getSessionProducts().add(sp);
    session.setUpdatedAt(now);
    session.setTotalAmount(session.getTotalAmount() + product.getPrice() * quantity);
    // update Inventory
    inventory.setInSessionHolding(inventory.getInSessionHolding() + quantity);
  }

  @Transactional
  public void updateCart(SessionProduct sp, Session session, Product product, Inventory inventory, int quantity, Date now) throws ResponseStatusException {
    int diff = quantity - sp.getCount();
    if (diff > inventory.getInStockCount()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "In stock limit exceeded!");
    }
    sp.setCount(quantity);
    inventory.setInSessionHolding(inventory.getInSessionHolding() + diff);
    session.setTotalAmount(session.getTotalAmount() + diff * product.getPrice());
    session.setUpdatedAt(now);
  }

  @Transactional
  public void deleteSingleProductFromCart(Session session, Product product, Inventory inventory) throws ResponseStatusException {
    // session-product
    SessionProduct sp;
    SessionProductKey spKey = new SessionProductKey(session.getId(), product.getId());
    Optional<SessionProduct> spOpt = spRepository.findById(spKey);
    if (spOpt.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not in session");
    sp = spOpt.get();
    int count = sp.getCount();
    session.setTotalAmount(session.getTotalAmount() - count * product.getPrice());
    inventory.setInSessionHolding(inventory.getInSessionHolding() - count);
    spRepository.delete(sp);
  }
}
