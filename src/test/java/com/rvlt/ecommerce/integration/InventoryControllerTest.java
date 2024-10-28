package com.rvlt.ecommerce.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvlt._common.constants.Constants;
import com.rvlt._common.model.Inventory;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class InventoryControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private InventoryRepository inventoryRepository;

  @BeforeAll
  public void setup() throws Exception {
    var rq = new CreateInventoryRq();
    rq.setName("IPhone 15 Pro Max");
    rq.setCount(10);

    var request = new RequestMessage<CreateInventoryRq>();
    request.setTime(new Date().toString());
    request.setData(rq);
    String inputJson = objectMapper.writeValueAsString(request);
    mockMvc.perform(post("/ec/inventories")
                    .content(inputJson)
                    .header(Constants.RVLT.userIdHeader, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  public void createNewInventoryAlsoCreateProduct() throws Exception {
    Inventory inventory = inventoryRepository.findByNameIgnoreCase("IPhone 15 Pro Max").get();

    assertNotNull(inventory);
    assertNotNull(inventory.getProduct());
    assertEquals(inventory.getId(), inventory.getProduct().getId());
    assertEquals(inventory.getName(), inventory.getProduct().getName());
  }

  @Test
  public void checkBalanceInventoryCounts() throws Exception {
    Inventory inventory = inventoryRepository.findByNameIgnoreCase("IPhone 15 Pro Max").get();
    assertNotNull(inventory);
    assertEquals(inventory.getTotalCount(), inventory.getInStockCount() + inventory.getDeliveredCount()
            + inventory.getProcessingSubmitCount() + inventory.getProcessingCancelCount() + inventory.getDeliveryInProgressCount() + inventory.getReturnInProgressCount()
            + inventory.getInSessionHolding());
  }
}
