package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.model.Inventory;

import java.util.List;

public interface InventoryService {
    ResponseMessage<List<Inventory>> getAllInventory();

    ResponseMessage<Inventory> getInventoryById(Long id);

    ResponseMessage<Void> deleteInventoryById(Long id);

    ResponseM
}
