package com.rvlt.ecommerce.rabbitmq;

import com.rvlt.ecommerce.dto.order.SubmitOrderRq;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class QueueItem {
  private String type;
  private SubmitOrderRq data;

  @Override
  public String toString() {
    return "QueueItem{" +
            "type='" + type + '\'' +
            ", data='" + data + '\'' +
            '}';
  }
}