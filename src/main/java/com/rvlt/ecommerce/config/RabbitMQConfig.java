package com.rvlt.ecommerce.config;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
  @Value("${spring.rabbitmq.template.exchange}")
  private String exchangeName;

  @Value("${spring.rabbitmq.queue}")
  private String orderQueue;

  @Value("${spring.rabbitmq.template.routing-key}")
  private String routingKey;

  @Value("${spring.rabbitmq.host}")
  private String host;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  @Bean
  public Queue orderQueue() {
    return new Queue(orderQueue, true);
  }

  @Bean
  public Exchange exchange() {
    return new DirectExchange(exchangeName);
  }

  @Bean
  public Binding binding(Queue queue, Exchange exchange) {
    return BindingBuilder.bind(queue)
            .to(exchange)
            .with(routingKey)
            .noargs();
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
    cachingConnectionFactory.setUsername(username);
    cachingConnectionFactory.setPassword(password);
    return cachingConnectionFactory;
  }


  // Queue message as object
  @Bean("Jackson2JsonMessageConverter")
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    return rabbitTemplate;
  }
}
