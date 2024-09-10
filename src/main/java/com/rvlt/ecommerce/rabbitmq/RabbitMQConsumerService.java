package com.rvlt.ecommerce.rabbitmq;

import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import com.rvlt._common.model.User;
import com.rvlt.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;

@Service
@RabbitListener(queues = "${spring.rabbitmq.queue}", messageConverter = "Jackson2JsonMessageConverter", returnExceptions = "true")
public class RabbitMQConsumerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumerService.class);

  @Autowired
  private OrderService orderService;

  @RabbitHandler
  @Transactional
  public void receiveMessage(QueueItem message) throws Exception {
    if (message.getType().equals("submit_order")) {
      mockOrder(message.getData());
    } else {
      LOGGER.info(String.format("Received message -> %s", message));
    }
  }

  @Transactional
  public void mockOrder(SubmitOrderRq rq) throws Exception {
    try {
      User currentUser = orderService.submitOrderAction(rq);
      Date now = new Date();
      orderService.afterOrderSubmitAction(currentUser, now);
    } catch (Exception e) {
//      System.out.println(">>>>>>>>>>" +e.getMessage());
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw e;
    }
  }
}
