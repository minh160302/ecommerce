package com.rvlt.ecommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvlt._common.constants.Constants;
import com.rvlt._common.model.Inventory;
import com.rvlt._common.model.User;
import com.rvlt._common.model.Wishlist;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.inventory.CreateInventoryRq;
import com.rvlt.ecommerce.dto.user.UserOnboardingRq;
import com.rvlt.ecommerce.dto.wishlist.HandleWishlistActionRq;
import com.rvlt.ecommerce.repository.InventoryRepository;
import com.rvlt.ecommerce.repository.UserRepository;
import com.rvlt.ecommerce.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class WishlistControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private InventoryRepository inventoryRepository;

  @Autowired
  private WishlistRepository wishlistRepository;

  private User user;
  private Inventory inventory;

  @BeforeAll
  public void setup() throws Exception {
    // onboard test user
    var onboardingRq = new UserOnboardingRq();
    onboardingRq.setFirstName("John");
    onboardingRq.setLastName("Doe");
    onboardingRq.setDob("02/20/2002");
    onboardingRq.setEmail("john@doe.com");

    RequestMessage<UserOnboardingRq> onboardRequest = new RequestMessage<>();
    onboardRequest.setTime(new Date().toString());
    onboardRequest.setData(onboardingRq);

    mockMvc.perform(post("/ec/users/onboarding")
                    .content(objectMapper.writeValueAsString(onboardRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated()).andReturn();

    user = userRepository.findByEmail(onboardingRq.getEmail()).get();

    // create inventory
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

    inventory = inventoryRepository.findByName("IPhone 15 Pro Max").get();
  }

  @AfterAll
  public void tearDown() throws Exception {
    mockMvc.perform(delete("/ec/inventories/{id}", inventory.getId())
                    .header(Constants.RVLT.userIdHeader, 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    userRepository.delete(user);
  }

  @Test
  public void addProductToWishlist() throws Exception {
    HandleWishlistActionRq rq = new HandleWishlistActionRq();
    rq.setProductId(inventory.getId().toString());

    RequestMessage<HandleWishlistActionRq> request = new RequestMessage<>();
    request.setTime(new Date().toString());
    request.setData(rq);

    mockMvc.perform(post("/ec/wishlist/add")
                    .content(objectMapper.writeValueAsString(request))
                    .header(Constants.RVLT.userIdHeader, user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    Wishlist wishlist = wishlistRepository.findWishlistByUserId(user.getId()).get();
    assertNotNull(wishlist);
    assertEquals(1, wishlist.getProducts().size());
    assertEquals(inventory.getId(), wishlist.getProducts().stream().toList().get(0).getId());
  }

  @Test
  public void removeProductFromWishlist() throws Exception {
    HandleWishlistActionRq rq = new HandleWishlistActionRq();
    rq.setProductId(inventory.getId().toString());

    RequestMessage<HandleWishlistActionRq> request = new RequestMessage<>();
    request.setTime(new Date().toString());
    request.setData(rq);

    mockMvc.perform(post("/ec/wishlist/remove")
                    .content(objectMapper.writeValueAsString(request))
                    .header(Constants.RVLT.userIdHeader, user.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    Wishlist wishlist = wishlistRepository.findWishlistByUserId(user.getId()).get();
    assertNotNull(wishlist);
    assertEquals(0, wishlist.getProducts().size());
  }
}
