package com.rvlt.ecommerce.controller;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.InventoryRf;
import com.rvlt.ecommerce.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventories")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("")
    public ResponseEntity<ResponseMessage<List<InventoryRf>>> getAllInventory() {
        ResponseMessage<List<InventoryRf>> res = inventoryService.getAllProductInInventory();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<ResponseMessage<InventoryRf>> getInventoryById(@PathVariable Long inventoryId) {
        ResponseMessage<InventoryRf> res = inventoryService.getProductInInventory(inventoryId);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
