package com.rvlt.ecommerce.service;

import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt._common.model.Inventory;
import com.rvlt._common.model.Product;
import com.rvlt.ecommerce.repository.InventoryRepository;
import com.rvlt.ecommerce.repository.ProductRepository;
import com.rvlt.ecommerce.utils.Validator;
import com.rvlt.ecommerce.utils.ExcelUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
  @Autowired
  private InventoryRepository inventoryRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private Validator validator;

  @Override
  public ResponseMessage<List<Inventory>> getAllInventory(HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    ResponseMessage<List<Inventory>> rs = new ResponseMessage<>();
    Status status = new Status();
    List<Inventory> inventoryList = inventoryRepository.findAll();
    rs.setData(inventoryList);
    rs.setStatus(status);
    return rs;
  }

  @Override
  public ResponseMessage<Inventory> getInventoryById(Long id, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    ResponseMessage<Inventory> rs = new ResponseMessage<>();
    Status status = new Status();
    Optional<Inventory> invOpt = inventoryRepository.findById(id);
    if (invOpt.isPresent()) {
      rs.setData(invOpt.get());
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> deleteInventoryById(Long id, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    if (inventoryRepository.existsById(id)) {
      inventoryRepository.deleteById(id);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> updateInventory(Long id, RequestMessage<UpdateInventoryRq> rq, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    UpdateInventoryRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Optional<Inventory> invOpt = inventoryRepository.findById(id);
    if (invOpt.isPresent()) {
      Inventory inventory = invOpt.get();
      inventory.setName(request.getName().trim());

      // update product
      inventory.getProduct().setName(inventory.getName());
      inventoryRepository.save(inventory);
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> importSingleInventory(RequestMessage<CreateInventoryRq> rq, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    CreateInventoryRq request = rq.getData();
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    Optional<Inventory> invOpt = inventoryRepository.findByName(request.getName());
    if (invOpt.isPresent()) {
      updateExistingInventory(invOpt.get(), request);
    } else {
      createNewInventory(request);
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> importBatchInventories(RequestMessage<CreateInventoryBatchRq> rq, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    CreateInventoryBatchRq request = rq.getData();
    // this is doing double count addition, somehow
//    this.aggregateBatchCreateInventory(request);
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    List<CreateInventoryRq> rqList = request.getInventories();
    if (rqList.size() > 20) {
      throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Batch import failed: too many inventories at once.");
    }
    for (CreateInventoryRq inventoryRq : request.getInventories()) {
      Optional<Inventory> invOpt = inventoryRepository.findByNameIgnoreCase(inventoryRq.getName());
      if (invOpt.isPresent()) {
        Inventory inventory = invOpt.get();
        updateExistingInventory(inventory, inventoryRq);
        System.out.println("Updated existing inventory: " + inventory.getName());
      } else {
        createNewInventory(inventoryRq);
        System.out.println("Created new inventory: " + inventoryRq.getName());
      }
    }
    rs.setStatus(status);
    return rs;
  }

  @Override
  @Transactional
  public ResponseMessage<Void> importBatchThroughExcel(MultipartFile file, HttpServletRequest httpServletRequest) {
    validator.validateAdmin(httpServletRequest);
    ResponseMessage<Void> rs = new ResponseMessage<>();
    Status status = new Status();
    try {
      ExcelUtils.validateExcelFile(file);
      InputStream inputStream = file.getInputStream();
      RequestMessage<CreateInventoryBatchRq> batchRequest = ExcelUtils.parseExcelToBatchRequest(inputStream);
      importBatchInventories(batchRequest, httpServletRequest);
    } catch (Exception e) {
      status.setHttpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
      status.setMessage("Excel import failed: " + e.getMessage());
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

    inventory.setProduct(product);
    product.setInventory(inventory);

    inventoryRepository.save(inventory);
    productRepository.save(product);
  }

  private void aggregateBatchCreateInventory(CreateInventoryBatchRq request) {
    List<CreateInventoryRq> rqList = request.getInventories();
    HashMap<String, Integer> freq = new HashMap<>();
    for (CreateInventoryRq inventoryRq : request.getInventories()) {
      String inventoryRqName = inventoryRq.getName().trim();
      int q = freq.getOrDefault(inventoryRqName, 0);
      freq.put(inventoryRqName, q + inventoryRq.getCount());
    }
    for (String key : freq.keySet()) {
      CreateInventoryRq rq = new CreateInventoryRq();
      rq.setName(key);
      rq.setCount(freq.get(key));
      rqList.add(rq);
    }
    request.setInventories(rqList);
  }
}
