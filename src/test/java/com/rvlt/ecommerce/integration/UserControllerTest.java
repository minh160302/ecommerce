package com.rvlt.ecommerce.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rvlt._common.constants.Constants;
import com.rvlt._common.model.Session;
import com.rvlt._common.model.User;
import com.rvlt._common.model.enums.OrderStatus;
import com.rvlt._common.model.enums.SessionStatus;
import com.rvlt.ecommerce.dto.RequestMessage;
import com.rvlt.ecommerce.dto.ResponseMessage;
import com.rvlt.ecommerce.dto.Status;
import com.rvlt.ecommerce.dto.user.UserOnboardingRq;
import com.rvlt.ecommerce.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  /**
   * When onboarding new user, expect also creating an ACTIVE session and NOT_SUBMITTED order
   * @throws Exception
   */
  @Test
  public void userOnBoardingIntegrationTest() throws Exception {
    var onboardingRq = new UserOnboardingRq();
    onboardingRq.setFirstName("John");
    onboardingRq.setLastName("Doe");
    onboardingRq.setDob("02/20/2002");
    onboardingRq.setEmail("john@doe.com");

    RequestMessage<UserOnboardingRq> request = new RequestMessage<>();
    request.setTime(new Date().toString());
    request.setData(onboardingRq);

    String inputJson = objectMapper.writeValueAsString(request);
    MvcResult mvcResult = mockMvc.perform(post("/ec/users/onboarding")
                    .content(inputJson)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated()).andReturn();

    String jsonResponse = mvcResult.getResponse().getContentAsString();
    ResponseMessage<Void> userRs = objectMapper.readValue(jsonResponse, new TypeReference<>() {
    });
    assertNull(userRs.getData());
    Status status = userRs.getStatus();

    assertEquals(201, status.getHttpStatusCode());
    assertEquals(Constants.SERVER_STATUS_CODE.SUCCESS, status.getServerStatusCode());

    var maybeUser = userRepository.findByEmail("john@doe.com");
    assertTrue(maybeUser.isPresent());

    // User
    User user = maybeUser.get();
    assertEquals("john@doe.com", user.getEmail());
    // Session
    assertEquals(1, user.getSessions().size());
    Session session = (Session) user.getSessions().toArray()[0];
    assertEquals(SessionStatus.ACTIVE, session.getStatus());
    // Order
    assertNotNull(session.getOrder());
    assertEquals(session.getId(), session.getOrder().getId());
    assertEquals(OrderStatus.NOT_SUBMITTED, session.getOrder().getStatus());
  }
}
