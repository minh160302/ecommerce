package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.service.InventoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
  @Autowired
  private InventoryService inventoryService;

  @GetMapping("")
  public ResponseEntity<ResponseMessage<List<Inventory>>> getAllInventory(HttpServletRequest httpServletRequest) {
    ResponseMessage<List<Inventory>> res = inventoryService.getAllInventory(httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @GetMapping("/{inventoryId}")
  public ResponseEntity<ResponseMessage<Inventory>> getInventoryById(@PathVariable Long inventoryId, HttpServletRequest httpServletRequest) {
    ResponseMessage<Inventory> res = inventoryService.getInventoryById(inventoryId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("")
  public ResponseEntity<ResponseMessage<Void>> importSingleInventory(@RequestBody RequestMessage<CreateInventoryRq> rq, HttpServletRequest httpServletRequest) {
      ResponseMessage<Void> res = inventoryService.importSingleInventory(rq, httpServletRequest);
      return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping("/batch")
  public ResponseEntity<ResponseMessage<Void>> importBatchInventories(@RequestBody RequestMessage<CreateInventoryBatchRq> request, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = inventoryService.importBatchInventories(request, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PostMapping(value = "/import-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseMessage<Void>> importBatchExcel(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest)  {
    ResponseMessage<Void> res = inventoryService.importBatchThroughExcel(file, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @DeleteMapping("/{inventoryId}")
  public ResponseEntity<ResponseMessage<Void>> deleteInventoryById(@PathVariable Long inventoryId, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = inventoryService.deleteInventoryById(inventoryId, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }

  @PutMapping("/{inventoryId}")
  public ResponseEntity<ResponseMessage<Void>> updateInventoryById(@PathVariable Long inventoryId, @RequestBody RequestMessage<UpdateInventoryRq> rq, HttpServletRequest httpServletRequest) {
    ResponseMessage<Void> res = inventoryService.updateInventory(inventoryId, rq, httpServletRequest);
    return new ResponseEntity<>(res, HttpStatus.valueOf(res.getStatus().getHttpStatusCode()));
  }
}
