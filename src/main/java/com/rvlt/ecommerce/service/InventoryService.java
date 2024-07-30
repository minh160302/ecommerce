package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InventoryService {
  ResponseMessage<List<Inventory>> getAllInventory();

  ResponseMessage<Inventory> getInventoryById(Long id);

  ResponseMessage<Void> deleteInventoryById(Long id);

  ResponseMessage<Void> updateInventory(Long id, RequestMessage<UpdateInventoryRq> rq);

  ResponseMessage<Void> importSingleInventory(RequestMessage<CreateInventoryRq> request);

  ResponseMessage<Void> importBatchInventories(RequestMessage<CreateInventoryBatchRq> request);

  ResponseMessage<Void> importBatchThroughExcel(MultipartFile file);
}
