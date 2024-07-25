package com.rvlt.ecommerce.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {
  @Value("${spring.rabbitmq.template.exchange}")
  private String exchangeName;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingKey;

  private final RabbitTemplate rabbitTemplate;

  public RabbitMQProducerService(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendMessage(String message) {
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
  }

  public void sendOrderMessage(QueueItem queueItem) {
    rabbitTemplate.convertAndSend(exchangeName, routingKey, queueItem);
  }
}
