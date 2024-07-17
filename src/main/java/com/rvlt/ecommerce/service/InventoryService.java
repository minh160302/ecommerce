package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import org.apache.coyote.Response;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public interface InventoryService {
  ResponseMessage<List<Inventory>> getAllInventory();

  ResponseMessage<Inventory> getInventoryById(Long id);

  ResponseMessage<Void> deleteInventoryById(Long id);

  ResponseMessage<Void> updateInventory(Long id, UpdateInventoryRq request);

  ResponseMessage<Void> importSingleInventory(CreateInventoryRq request);

  ResponseMessage<Void> importBatchInventories(CreateInventoryBatchRq request);

  ResponseMessage<Void> importBatchThroughExcel(InputStream file);
}
