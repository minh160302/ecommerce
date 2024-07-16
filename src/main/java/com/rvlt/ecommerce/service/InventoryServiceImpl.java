package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.model.Product;
import com.rvlt.ecommerce.repository.InventoryRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
  @Autowired
  private InventoryRepository inventoryRepository;

  @Autowired
  private ProductRepository productRepository;

  @Override
  public ResponseMessage<List<Inventory>> getAllInventory() {
    ResponseMessage<List<Inventory>> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      List<Inventory> inventoryList = inventoryRepository.findAll();
      rs.setData(inventoryList);
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage(e.getMessage());
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Inventory> getInventoryById(Long id) {
    ResponseMessage<Inventory> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Optional<Inventory> invOpt = inventoryRepository.findById(id);
      if (invOpt.isPresent()) {
        rs.setData(invOpt.get());
      } else {
        status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
        status.setMessage("Inventory not found");
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
  public ResponseMessage<Void> deleteInventoryById(Long id) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      if (inventoryRepository.existsById(id)) {
        inventoryRepository.deleteById(id);
      } else {
        status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
        status.setMessage("Inventory not found");
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
  public ResponseMessage<Void> updateInventory(Long id, UpdateInventoryRq request) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Optional<Inventory> invOpt = inventoryRepository.findById(id);
      if (invOpt.isPresent()) {
        Inventory inventory = invOpt.get();
        inventory.setName(request.getName().trim());
        inventoryRepository.save(inventory);
      } else {
        status.setHttpStatusCode(String.valueOf(HttpStatus.NOT_FOUND.value()));
        status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
        status.setMessage("Inventory not found");
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
  public ResponseMessage<Void> importSingleInventory(CreateInventoryRq request) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      Optional<Inventory> invOpt = inventoryRepository.findByName(request.getName());
      if (invOpt.isPresent()) {
        updateExistingInventory(invOpt.get(), request);
      } else {
        createNewInventory(request);
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
  public ResponseMessage<Void> importBatchInventories(CreateInventoryBatchRq request) {
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      List<CreateInventoryRq> rqList = request.getInventories();
      if (rqList.size() > 100) {
        throw new Exception("Import too many inventories at once.");
      }
      for (CreateInventoryRq inventoryRq : request.getInventories()) {
        Optional<Inventory> invOpt = inventoryRepository.findByName(inventoryRq.getName());
        if (invOpt.isPresent()) {
          Inventory inventory = invOpt.get();
          updateExistingInventory(inventory, inventoryRq);
        } else {
          createNewInventory(inventoryRq);
        }
      }
    } catch (Exception e) {
      status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage("Batch import failed:" + e.getMessage());
      // Manually trigger rollback
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    rs.setStatus(status);
    return rs;
  }

  private void updateExistingInventory(Inventory inventory, CreateInventoryRq request) {
    int count = request.getCount();
    inventory.setTotalCount(inventory.getTotalCount() + count);
    inventory.setInStockCount(inventory.getInStockCount() + count);
    inventoryRepository.save(inventory);
    Product product = inventory.getProduct();
    product.setInStock(inventory.getInStockCount());
    productRepository.save(product);
  }

  private void createNewInventory(CreateInventoryRq request) {
    Inventory inventory = new Inventory();
    inventory.setName(request.getName());
    inventory.initializeInventory(request.getCount());

    Product product = new Product();
    product.setName(inventory.getName());
    product.setPrice(-1);
    product.setInStock(inventory.getInStockCount());
    product.setInventory(inventory);

    inventoryRepository.save(inventory);
    productRepository.save(product);
  }
}
