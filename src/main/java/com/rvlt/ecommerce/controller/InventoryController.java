package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryBatchRq;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.service.InventoryService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
  @Autowired
  private InventoryService inventoryService;

  @GetMapping("")
  public ResponseEntity<ResponseMessage<List<Inventory>>> getAllInventory() {
    ResponseMessage<List<Inventory>> res = inventoryService.getAllInventory();
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @GetMapping("/{inventoryId}")
  public ResponseEntity<ResponseMessage<Inventory>> getInventoryById(@PathVariable Long inventoryId) {
    ResponseMessage<Inventory> res = inventoryService.getInventoryById(inventoryId);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<ResponseMessage<Void>> importSingleInventory(@RequestBody RequestMessage<CreateInventoryRq> rq) {
      ResponseMessage<Void> res = inventoryService.importSingleInventory(rq);
      return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping("/batch")
  public ResponseEntity<ResponseMessage<Void>> importBatchInventories(@RequestBody RequestMessage<CreateInventoryBatchRq> request) {
    ResponseMessage<Void> res = inventoryService.importBatchInventories(request);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PostMapping(value = "/import-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ResponseMessage<Void>> importBatchExcel(@RequestParam("file") MultipartFile file)  {
    ResponseMessage<Void> res = inventoryService.importBatchThroughExcel(file);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @DeleteMapping("/{inventoryId}")
  public ResponseEntity<ResponseMessage<Void>> deleteInventoryById(@PathVariable Long inventoryId) {
    ResponseMessage<Void> res = inventoryService.deleteInventoryById(inventoryId);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }

  @PutMapping("/{inventoryId}")
  public ResponseEntity<ResponseMessage<Void>> updateInventoryById(@PathVariable Long inventoryId, @RequestBody RequestMessage<UpdateInventoryRq> rq) {
    ResponseMessage<Void> res = inventoryService.updateInventory(inventoryId, rq);
    return new ResponseEntity<>(res, HttpStatus.OK);
  }
}
