package com.rvlt.ecommerce.rabbitmq;

import com.rvlt.ecommerce.model.Inventory;
import com.rvlt.ecommerce.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "${spring.rabbitmq.queue}", messageConverter = "Jackson2JsonMessageConverter")
public class RabbitMQConsumerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumerService.class);

  @Autowired
  private InventoryRepository inventoryRepository;

  @RabbitHandler
  public void receiveMessage(QueueItem message) throws Exception {
    if (message.getType().equals("submit_order")) {
      mockOrder(Long.valueOf(message.getData()));
    } else {
      LOGGER.info(String.format("Received message -> %s", message));
    }
  }


  private void mockOrder(Long inventoryId) throws Exception {
    Inventory inventory = inventoryRepository.findById(inventoryId).get();
    int cnt = inventory.getInStockCount();
    if (cnt > 10) {
      inventory.setInStockCount(cnt - 10);
      inventoryRepository.save(inventory);
      LOGGER.info("Successfully submitted order");
    } else {
//      throw new Exception();
      throw new AmqpRejectAndDontRequeueException("Retry exceeded. Insufficient item: " + inventoryId + " Amount: " + inventory.getInStockCount());
    }
  }
}
