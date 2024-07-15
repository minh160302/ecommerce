package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.inventory.InventoryRf;
import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public ResponseMessage<List<InventoryRf>> getAllProductInInventory() {
        ResponseMessage<List<InventoryRf>> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            List<Inventory> inventoryList = inventoryRepository.findAll();
            List<InventoryRf> inventoryRfList = new ArrayList<>();
            for (Inventory inventory : inventoryList) {
                InventoryRf inventoryRf = mapToInventoryRf(inventory);
                inventoryRfList.add(inventoryRf);
            }
            rs.setData(inventoryRfList);
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }

    @Override
    public ResponseMessage<InventoryRf> getProductInInventory(Long id) {
        ResponseMessage<InventoryRf> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()) {
                InventoryRf inventoryRf = mapToInventoryRf(inventory.get());
                rs.setData(inventoryRf);
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

//    @Override
//    @Transactional
//    public ResponseMessage<Void> deleteProductInInventory(Long id) {
//        ResponseMessage<Void> rs = new ResponseMessage<>();
//        Status status = new Status();
//
//    }

    private InventoryRf mapToInventoryRf(Inventory inventory) {
        InventoryRf rf = new InventoryRf();
        rf.setId(inventory.getId());
        rf.setName(inventory.getName());
        rf.setTotalCount(inventory.getTotalCount());
        rf.setInStockCount(inventory.getInStockCount());
        rf.setProcessingCount(inventory.getProcessingCount());
        rf.setDeliveredCount(inventory.getDeliveredCount());
        rf.setInSessionHolding(inventory.getInSessionHolding());
        rf.setBalance(inventory.getBalance());
        return rf;
    }
}
