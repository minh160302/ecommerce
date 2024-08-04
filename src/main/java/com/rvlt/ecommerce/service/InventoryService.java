package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InventoryService {
  ResponseMessage<List<Inventory>> getAllInventory(HttpServletRequest httpServletRequest);

  ResponseMessage<Inventory> getInventoryById(Long id, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> deleteInventoryById(Long id, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> updateInventory(Long id, RequestMessage<UpdateInventoryRq> rq, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> importSingleInventory(RequestMessage<CreateInventoryRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> importBatchInventories(RequestMessage<CreateInventoryBatchRq> request, HttpServletRequest httpServletRequest);

  ResponseMessage<Void> importBatchThroughExcel(MultipartFile file, HttpServletRequest httpServletRequest);
}
