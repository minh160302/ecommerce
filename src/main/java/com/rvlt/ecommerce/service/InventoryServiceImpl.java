package com.rvlt.ecommerce.service;

import com.rvlt.ecommerce.constants.Constants;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.inventory.UpdateInventoryRq;
import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public ResponseMessage<List<Inventory>> getAllInventory() {
        ResponseMessage<List<Inventory>> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            List<Inventory> inventoryList = inventoryRepository.findAll();
            rs.setData(inventoryList);
        } catch (Exception e) {
            status.setHttpStatusCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
            status.setServerStatusCode(Constants.SERVER_STATUS_CODE.FAILED);
            status.setMessage(e.getMessage());
        }
        rs.setStatus(status);
        return rs;
    }

    @Override
    public ResponseMessage<Inventory> getInventoryById(Long id) {
        ResponseMessage<Inventory> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()) {
                rs.setData(inventory.get());
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

    @Override
    @Transactional
    public ResponseMessage<Void> deleteInventoryById(Long id) {
        ResponseMessage<Void> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            if(inventoryRepository.existsById(id)) {
                inventoryRepository.deleteById(id);
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

    @Override
    @Transactional
    public ResponseMessage<Void> updateInventory(Long id, UpdateInventoryRq request) {
        ResponseMessage<Void> rs = new ResponseMessage<>();
        Status status = new Status();
        try {
            Optional<Inventory> inventory = inventoryRepository.findById(id);
            if (inventory.isPresent()) {
                Inventory inventoryObj = inventory.get();

                // check if input has whitespace or empty
                // kind of redundant, this should be client-side
                if (request.getName() != null && !request.getName().trim().isEmpty()) {
                    inventoryObj.setName(request.getName().trim());
                }

                inventoryRepository.save(inventoryObj);

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
}
