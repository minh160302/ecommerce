package com.rvlt.ecommerce.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.RemoteInvocationResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

  // sendAndReceive
  public void sendOrderMessage(QueueItem queueItem) throws AmqpException {
    RemoteInvocationResult res = rabbitTemplate.convertSendAndReceiveAsType(exchangeName, routingKey, queueItem, new ParameterizedTypeReference<>() {});
    if (res != null && res.getException() != null) {
      throw new AmqpException(res.getException());
    }
    else if (res != null && res.getValue() != null) {
      System.out.println("MQ Receiving message: " + res.getValue());
    }
  }
}
