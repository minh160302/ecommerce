package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.inventory.InventoryRf;

import java.util.List;

public interface InventoryService {
    ResponseMessage<List<InventoryRf>> getAllProductInInventory();

    ResponseMessage<InventoryRf> getProductInInventory(Long id);

//    ResponseMessage<Void> deleteProductInInventory(Long id);

//    ResponseMessage<Integer> getProductTotalCountById(Long id);
}
