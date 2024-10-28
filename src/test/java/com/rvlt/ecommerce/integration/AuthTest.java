package com.rvlt.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvlt._common.constants.Constants;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class AuthTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private InventoryRepository inventoryRepository;

  /**
   * Actions on inventory require admin authorization.
   */
  @Test
  public void inventoryRequiresAdminRole() throws Exception {
    var rq = new CreateInventoryRq();
    rq.setName("IPhone 15 Pro Max");
    rq.setCount(10);

    var request = new RequestMessage<CreateInventoryRq>();
    request.setTime(new Date().toString());
    request.setData(rq);
    String inputJson = objectMapper.writeValueAsString(request);

    // No admin header
    mockMvc.perform(post("/ec/inventories")
                    .content(inputJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    // Unauthorized user
    mockMvc.perform(post("/ec/inventories")
                    .content(inputJson)
                    .header(Constants.RVLT.userIdHeader, 2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    // Wrong format admin header
    mockMvc.perform(post("/ec/inventories")
                    .content(inputJson)
                    .header(Constants.RVLT.userIdHeader, "Not_an_integer_user_id.")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError());

    // Success
    mockMvc.perform(post("/ec/inventories")
                    .content(inputJson)
                    .header(Constants.RVLT.userIdHeader, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    mockMvc.perform(get("/ec/inventories")
                    .content(inputJson)
                    .header(Constants.RVLT.userIdHeader, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }
}
